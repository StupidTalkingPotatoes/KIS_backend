package org.stupid_talking_potatoes.kis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.stupid_talking_potatoes.kis.dto.path.Path;
import org.stupid_talking_potatoes.kis.dto.path.Step;
import org.stupid_talking_potatoes.kis.dto.route.ArrivalRoute;
import org.stupid_talking_potatoes.kis.entity.Node;
import org.stupid_talking_potatoes.kis.repository.NodeRepository;
import org.stupid_talking_potatoes.kis.service.tago.TagoBaseService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

/**
 * package :  org.stupid_talking_potatoes.kis.naver.service
 * fileName : NaverService
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NaverService {
    private final NodeRepository nodeRepository;
    
    /**
     * 최종적으로 완성된 Path List를 만드는 메서드
     *
     * @param departureLongitude 출발지 경도
     * @param departureLatitude  출발지 위도
     * @param arrivalLongitude   도착지 경도
     * @param arrivalLatitude    출발지 위도
     * @return {@link ArrayList}
     * @see Path
     */
    public ArrayList<Path> getTransportationInfo(Double departureLongitude, Double departureLatitude, Double arrivalLongitude, Double arrivalLatitude) {
        JSONObject ptMapJSON = getPtMapJSON(departureLongitude, departureLatitude, arrivalLongitude, arrivalLatitude);
        JSONArray paths = ptMapJSON.getJSONObject("res").getJSONArray("paths");
        JSONArray staticPaths = ptMapJSON.getJSONObject("res").getJSONArray("staticPaths");
        
        ArrayList<Path> pathList = setPathList(paths);
        ArrayList<Path> staticPathList = setPathList(staticPaths);
        pathList.addAll(staticPathList);
        filterPath(pathList);
        return pathList;
    }
    
    /**
     * 각 path의 정보를 객체화
     * @param paths
     * @return
     */
    public ArrayList<Path> setPathList(JSONArray paths) {
        ArrayList<Path> pathList = new ArrayList<>();
        for (Object pathObj : paths) {
            JSONObject pathJSON = (JSONObject) pathObj;
            Path path = Path.ofJSON(pathJSON);
            setStepListByJSON(pathJSON, path);
            pathList.add(path);
        }
        return pathList;
    }
    
    /**
     * 각 path의 정보들을 기준으로 Step객체들을 생성하여 path에 저장
     *
     * @param pathJSON
     * @param path
     */
    private void setStepListByJSON(JSONObject pathJSON, Path path) {
        List<Step> stepList = new ArrayList<>();
        JSONArray legs = pathJSON.getJSONArray("legs");
        for (Object legObj : legs) {
            JSONObject leg = (JSONObject) legObj;
            setEachStep(stepList, leg);
        }
        path.setStepList(stepList);
    }
    
    /**
     * 각 step에 대한 check
     * @param stepList
     * @param leg
     */
    private void setEachStep(List<Step> stepList, JSONObject leg) {
        JSONArray steps = leg.getJSONArray("steps");
        for (Object stepObj : steps) {
            JSONObject step = (JSONObject) stepObj;
            String type = (String) step.get("type");
            
            if (!type.equals("BUS") && !type.equals("WALKING"))
                continue;
            
            if (type.equals("BUS"))
                setBusToStepList(stepList, step);
            
            if (type.equals("WALKING"))
                stepList.add(Step.ofJSON(step));
        }
    }
    
    /**
     * json의 step의 type이 BUS일때 Step객체 생성
     *
     * @param stepList stepList
     * @param stepJSON stepJSON
     */
    private void setBusToStepList(List<Step> stepList, JSONObject stepJSON) {
        Step step = Step.ofJSON(stepJSON);
        JSONArray stations = stepJSON.getJSONArray("stations");
        JSONObject departureJSON = (JSONObject) stations.get(0);
        JSONObject arrivalJSON = (JSONObject) stations.get(stations.toList().size() - 1);
        String departureDisplayCode = (String) departureJSON.get("displayCode");
        String arrivalDisplayCode = (String) arrivalJSON.get("displayCode");
        
        step.setDeparture(Objects.requireNonNull(getNode(departureDisplayCode)).getNodeName());
        step.setArrival(Objects.requireNonNull(getNode(arrivalDisplayCode)).getNodeName());
        
        List<ArrivalRoute> arrivalRouteList = new ArrayList<>();
        JSONArray arrivals = stepJSON.getJSONArray("arrivals");
        for (Object o : arrivals) {
            JSONObject arrival = (JSONObject) o;
            if (arrival.get("status").equals("RUNNING")) {
                JSONArray items = arrival.getJSONArray("items");
                for (Object o1 : items) {
                    JSONObject item = (JSONObject) o1;
                    if (item.get("lowFloor").equals(true)) {
                        ArrivalRoute arrivalRoute = ArrivalRoute.builder()
                                .prevNodeCnt((Integer) item.get("remainingStop"))
                                .arrTime(TagoBaseService.convertSecToMin((Integer) item.get("remainingTime")))
                                .build();
                        
                        //routeId랑 routeNo를 넣어줘야하는데..
                        Integer naverRouteIdByArrival = (Integer) arrival.get("routeId");
                        JSONArray routes = stepJSON.getJSONArray("routes");
                        for (Object o2 : routes) {
                            JSONObject routeJSON = (JSONObject) o2;
                            Integer naverRouteIdByRoute = (Integer) routeJSON.get("id");
                            if(naverRouteIdByRoute.equals(naverRouteIdByArrival))
                                arrivalRoute.setRouteNo((String) routeJSON.get("name"));
                        }
                        arrivalRouteList.add(arrivalRoute);
                    }
                }
            }
        }
        if (arrivalRouteList.isEmpty())
            arrivalRouteList = null;
        step.setArrivalRouteList(arrivalRouteList);
        stepList.add(step);
    }
    
    private Node getNode(String nodeNo) {
        Optional<Node> optional = nodeRepository.findByNodeNo(nodeNo);
        return optional.orElse(null);
    }
    
    /**
     * 네이버 빠른길찾기로 요청하여 JSON 형식으로 받아옴
     *
     * @param departureLongitude departureLongitude
     * @param departureLatitude  departureLatitude
     * @param arrivalLongitude   arrivalLongitude
     * @param arrivalLatitude    arrivalLatitude
     * @return {@link JSONObject}
     */
    public JSONObject getPtMapJSON(Double departureLongitude, Double departureLatitude, Double arrivalLongitude, Double arrivalLatitude) {
        String url = buildUrl(departureLongitude, departureLatitude, arrivalLongitude, arrivalLatitude);
        try {
            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
//            log.info("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            try {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                conn.disconnect();
                
                return new JSONObject(sb.toString());
            } catch (JSONException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 요청보낼 url을 합쳐줌
     *
     * @param departureLongitude departureLongitude
     * @param departureLatitude  departureLatitude
     * @param arrivalLongitude   arrivalLongitude
     * @param arrivalLatitude    arrivalLatitude
     * @return {@link String}
     */
    public static String buildUrl(Double departureLongitude, Double departureLatitude, Double arrivalLongitude, Double arrivalLatitude) {
        String baseUrl = "https://pt.map.naver.com/api/pubtrans-route-search";
//        String departureTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        LocalDateTime departureTime = LocalDateTime.of(2023, 5, 29, 20, 35);
        return baseUrl + "?phase=real" +
                "&mode=TIME" +
                "&departureTime=" + departureTime +
                "&departure=" + departureLongitude + "," + departureLatitude +
                "&arrival=" + arrivalLongitude + "," + arrivalLatitude +
                "&includeDetailOperation=true" +
                "&caller=cs-pc-quick-path";
    }
    
    
    /**
     * 어떠한 step이라도 arrival route list에 아무것도 없으면 해당 path를 제거
     * @param paths
     */
    private void filterPath(List<Path> paths) {
        Iterator<Path> iterator = paths.iterator();
        while (iterator.hasNext()) {
            Path path = iterator.next();
            List<Step> stepList = path.getStepList();
            for (Step step : stepList) {
                if (step.getType().equals("BUS") && step.getArrivalRouteList() == null) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

}