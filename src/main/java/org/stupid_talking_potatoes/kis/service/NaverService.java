package org.stupid_talking_potatoes.kis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.stupid_talking_potatoes.kis.dto.path.Path;
import org.stupid_talking_potatoes.kis.dto.path.Step;
import org.stupid_talking_potatoes.kis.dto.route.ArrivalRoute;
import org.stupid_talking_potatoes.kis.entity.Node;
import org.stupid_talking_potatoes.kis.exception.InvalidUrlException;
import org.stupid_talking_potatoes.kis.exception.NoContentException;
import org.stupid_talking_potatoes.kis.exception.NotFoundException;
import org.stupid_talking_potatoes.kis.exception.ThirdPartyAPIException;
import org.stupid_talking_potatoes.kis.repository.NodeRepository;
import org.stupid_talking_potatoes.kis.service.tago.TagoBaseService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NaverService {
    private final NodeRepository nodeRepository;
    
    public ArrayList<Path> getTransportationInfo(Double departureLongitude, Double departureLatitude, Double arrivalLongitude, Double arrivalLatitude) throws IOException {
        JSONObject ptMapJSON = getPtMapJSON(departureLongitude, departureLatitude, arrivalLongitude, arrivalLatitude);
        JSONArray paths = ptMapJSON.getJSONObject("res").getJSONArray("paths");
        JSONArray staticPaths = ptMapJSON.getJSONObject("res").getJSONArray("staticPaths");
        
        ArrayList<Path> pathList = setPathList(paths);
        ArrayList<Path> staticPathList = setPathList(staticPaths);
        pathList.addAll(staticPathList);
        filterPath(pathList);
        return pathList;
    }
    
    public ArrayList<Path> setPathList(JSONArray paths) {
        return paths.toList().stream()
                .map(pathObj -> (JSONObject) pathObj)
                .map(pathJSON -> {
                    Path path = Path.ofJSON(pathJSON);
                    setStepListByJSON(pathJSON, path);
                    return path;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    private void setStepListByJSON(JSONObject pathJSON, Path path) {
        JSONArray legs = pathJSON.getJSONArray("legs");
        List<Step> stepList = legs.toList().stream()
                .map(legObj -> (JSONObject) legObj)
                .flatMap(leg -> {
                    List<Step> steps = new ArrayList<>();
                    JSONArray stepArray = leg.getJSONArray("steps");
                    for (Object stepObj : stepArray) {
                        JSONObject stepJSON = (JSONObject) stepObj;
                        String type = (String) stepJSON.get("type");
    
                        if (!type.equals("BUS") && !type.equals("WALKING"))
                            continue;
                        
                        if (type.equals("BUS")) {
                            Step step = Step.ofJSON(stepJSON);
                            setBusToStepList(step, stepJSON);
                            steps.add(step);
                        }
                        
                        if (type.equals("WALKING")) {
                            steps.add(Step.ofJSON(stepJSON));
                        }
                    }
                    return steps.stream();
                })
                .collect(Collectors.toList());
        
        path.setStepList(stepList);
    }
    
    private void setBusToStepList(Step step, JSONObject stepJSON) {
        JSONArray stations = stepJSON.getJSONArray("stations");
        JSONObject departureJSON = (JSONObject) stations.get(0);
        JSONObject arrivalJSON = (JSONObject) stations.get(stations.toList().size() - 1);
        String departureDisplayCode = (String) departureJSON.get("displayCode");
        String arrivalDisplayCode = (String) arrivalJSON.get("displayCode");
        
        step.setDeparture(Objects.requireNonNull(getNode(departureDisplayCode)).getNodeName());
        step.setArrival(Objects.requireNonNull(getNode(arrivalDisplayCode)).getNodeName());
        
        List<ArrivalRoute> arrivalRouteList = createArrivalRouteList(stepJSON);
        step.setArrivalRouteList(arrivalRouteList);
    }
    
    private List<ArrivalRoute> createArrivalRouteList(JSONObject stepJSON) {
        JSONArray arrivals = stepJSON.getJSONArray("arrivals");
        JSONArray routes = stepJSON.getJSONArray("routes");
        
        return arrivals.toList().stream()
                .map(o -> (JSONObject) o)
                .filter(arrival -> arrival.get("status").equals("RUNNING"))
                .flatMap(arrival -> {
                    JSONArray items = arrival.getJSONArray("items");
                    JSONArray filteredItems = filterItemsByLowFloor(items);
                    List<ArrivalRoute> filteredRoutes = filterRoutesByArrivalId(routes, arrival);
                    return createArrivalRoutesFromJSON(filteredItems, filteredRoutes).stream();
                })
                .collect(Collectors.toList());
    }
    
    private JSONArray filterItemsByLowFloor(JSONArray items) {
        List<JSONObject> filteredItems = items.toList().stream()
                .map(obj -> (JSONObject) obj)
                .filter(item -> item.get("lowFloor").equals(true))
                .collect(Collectors.toList());
        return new JSONArray(filteredItems);
    }
    
    private List<ArrivalRoute> filterRoutesByArrivalId(JSONArray routes, JSONObject arrival) {
        Integer naverRouteIdByArrival = (Integer) arrival.get("routeId");
        
        return routes.toList().stream()
                .map(o -> (JSONObject) o)
                .filter(routeJSON -> {
                    Integer naverRouteIdByRoute = (Integer) routeJSON.get("id");
                    return naverRouteIdByRoute.equals(naverRouteIdByArrival);
                })
                .map(routeJSON -> ArrivalRoute.builder()
                        .prevNodeCnt((Integer) arrival.get("remainingStop"))
                        .arrTime(TagoBaseService.convertSecToMin((Integer) arrival.get("remainingTime")))
                        .routeNo((String) routeJSON.get("name"))
                        .build())
                .collect(Collectors.toList());
    }
    
    private List<ArrivalRoute> createArrivalRoutesFromJSON(JSONArray items, List<ArrivalRoute> filteredRoutes) {
        return items.toList().stream()
                .map(o -> (JSONObject) o)
                .map(item -> ArrivalRoute.builder()
                        .prevNodeCnt((Integer) item.get("remainingStop"))
                        .arrTime(TagoBaseService.convertSecToMin((Integer) item.get("remainingTime")))
                        .build())
                .collect(Collectors.toList());
    }
    
    private Node getNode(String nodeNo) {
        try {
            Optional<Node> optional = nodeRepository.findByNodeNo(nodeNo);
            return optional.orElseThrow(NoSuchElementException::new);
        } catch (NoSuchElementException e) {
            throw new NoContentException("Node not found",e.getMessage());
        }
    }
    
    public JSONObject getPtMapJSON(Double departureLongitude, Double departureLatitude, Double arrivalLongitude, Double arrivalLatitude) throws NotFoundException, IOException {
        String url = buildUrl(departureLongitude, departureLatitude, arrivalLongitude, arrivalLatitude);
        URL urlObj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            throw new ThirdPartyAPIException("naver API response is not OK.", HttpStatus.INTERNAL_SERVER_ERROR.name());
        }
        
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        
        rd.close();
        conn.disconnect();
        
        return new JSONObject(sb.toString());
    }
    
    public static String buildUrl(Double departureLongitude, Double departureLatitude, Double arrivalLongitude, Double arrivalLatitude) {
        String baseUrl = "https://pt.map.naver.com/api/pubtrans-route-search";
        String departureTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        
        return baseUrl + "?phase=real" +
                "&mode=TIME" +
                "&departureTime=" + departureTime +
                "&departure=" + departureLongitude + "," + departureLatitude +
                "&arrival=" + arrivalLongitude + "," + arrivalLatitude +
                "&includeDetailOperation=true" +
                "&caller=cs-pc-quick-path";
    }
    
    private void filterPath(List<Path> paths) {
        Iterator<Path> iterator = paths.iterator();
        while (iterator.hasNext()) {
            Path path = iterator.next();
            List<Step> stepList = path.getStepList();
            boolean hasArrivalRoute = stepList.stream()
                    .anyMatch(step -> step.getType().equals("BUS") && step.getArrivalRouteList() != null);
            if (!hasArrivalRoute) {
                iterator.remove();
            }
        }
    }
}
