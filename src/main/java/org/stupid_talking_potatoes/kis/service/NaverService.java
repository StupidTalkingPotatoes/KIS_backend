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
    private final NodeService nodeService;
    
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
        // setPathList: path들에 대해 stepList를 추가하여 path list 반환

        ArrayList<Path> pathList = new ArrayList<>();
        for (Object pathObj : paths) {
            JSONObject pathJSON = (JSONObject) pathObj;
            Path path = getPathByJson(pathJSON); // getPathByJson: stepList가 set 된 path 반환
            pathList.add(path);
        }
        return pathList;
    }

    private Path getPathByJson(JSONObject pathJson) {
        // getPathByJson: stepList이 추가된 path 반환

        Path path = Path.ofJSON(pathJson);  // base path
        List<Step> stepList = new ArrayList<>(); // new step list

        // for leg array
        JSONArray legArray = pathJson.getJSONArray("legs");
        for (Object legObj : legArray) {

            // for step array
            JSONArray stepArray = ((JSONObject) legObj).getJSONArray("steps");
            for (Object stepObj : stepArray) {
                JSONObject stepJSON = (JSONObject) stepObj;

                // check type of step
                String type = stepJSON.get("type").toString();
                if (type.equals("BUS")) {
                    Step step = getStepByJson(stepJSON);    // getStepByJson: bus가 set 된 step 반환
                    stepList.add(step);
                }
                else if (type.equals("WALKING")) {
                    Step step = Step.ofJSON(stepJSON);
                    stepList.add(step);
                }
            }
        }
        path.setStepList(stepList);
        return path;
    }

    private String getNodeNameFromJson(JSONObject json) {
        // getNodeNameFromJson: 정류장 json에서 no를 찾아 name을 구하고 반환

        String displayCode = json.get("displayCode").toString();
        Node node = nodeService.getNodeByNodeNo(displayCode);
        return node.getNodeName();
    }

    private Step getStepByJson(JSONObject stepJson) {
        // getStepByJson: bus 정보가 추가된 step 반환

        Step step = Step.ofJSON(stepJson); // base step

        // stations: bus를 타고 이동하는 정거장 리스트
        JSONArray stations = stepJson.getJSONArray("stations");

        // stations에서 처음, 마지막 원소를 가져옴 -> 각각이 bus를 이용하는 step의 출발지, 도착지
        JSONObject departureJSON = (JSONObject) stations.get(0);
        JSONObject arrivalJSON = (JSONObject) stations.get(stations.toList().size() - 1);

        // get & set node name
        step.setDeparture(getNodeNameFromJson(departureJSON));
        step.setArrival(getNodeNameFromJson(arrivalJSON));

        // 버스 도착 예정 정보를 담아서 반환
        step.setArrivalRouteList(createArrivalRouteList(stepJson));
        return step;
    }

    private ArrivalRoute getArrivalRouteByItemJson(JSONObject itemJson) {
        Integer arrTimeSec = (Integer) itemJson.get("remainingTime");
        Integer arrTimeMin = TagoBaseService.convertSecToMin(arrTimeSec);
        ArrivalRoute arrivalRoute = ArrivalRoute.builder()
                .prevNodeCnt((Integer) itemJson.get("remainingStop"))
                .arrTime(arrTimeMin)
                .build();
        return arrivalRoute;
    }

    private List<ArrivalRoute> createArrivalRouteList(JSONObject stepJson) {
        // createArrivalRouteList: step json에서 실시간 버스 도착 정보를 가져옴

        List<ArrivalRoute> arrivalRouteList = new ArrayList<>();  // base list

        JSONArray arrivalArray = stepJson.getJSONArray("arrivals");
        for (Object arrivalObj : arrivalArray) {
            JSONObject arrival = (JSONObject) arrivalObj;

            // check status of result
            if (!arrival.get("status").equals("RUNNING")) continue;

            JSONArray itemArray = arrival.getJSONArray("items");
            for (Object itemObj : itemArray) {
                JSONObject item = (JSONObject) itemObj;

                // check low floor
                if (!item.get("lowFloor").equals(true)) continue;

                // get & add arrival Route
                ArrivalRoute arrivalRoute = getArrivalRouteByItemJson(item); // base arrival route

                // set route's id and no
                Integer routeIdOfArrival = (Integer) arrival.get("routeId");
                JSONArray routeArray = stepJson.getJSONArray("routes");
                for (Object routeObj : routeArray) {
                    JSONObject routeJSON = (JSONObject) routeObj;
                    Integer routeIdOfRoute = (Integer) routeJSON.get("id");
                    if (routeIdOfRoute.equals(routeIdOfArrival))
                        arrivalRoute.setRouteNo((String) routeJSON.get("name"));
                }
                arrivalRouteList.add(arrivalRoute);
            }
        }
        if (arrivalRouteList.isEmpty())
            arrivalRouteList = null;
        return arrivalRouteList;
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
