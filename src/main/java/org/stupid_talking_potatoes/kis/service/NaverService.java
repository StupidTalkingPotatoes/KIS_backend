package org.stupid_talking_potatoes.kis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.stupid_talking_potatoes.kis.dto.path.Path;
import org.stupid_talking_potatoes.kis.dto.path.Step;
import org.stupid_talking_potatoes.kis.dto.route.ArrivalRoute;
import org.stupid_talking_potatoes.kis.entity.Node;
import org.stupid_talking_potatoes.kis.entity.Route;
import org.stupid_talking_potatoes.kis.repository.NodeRepository;
import org.stupid_talking_potatoes.kis.repository.RouteRepository;
import org.stupid_talking_potatoes.kis.repository.RouteSeqRepository;

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
    private final NodeService nodeService;
    private final RouteRepository routeRepository;
    
    private final RouteSeqRepository routeSeqRepository;
    
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
        ArrayList<Path> pathList = new ArrayList<>();
        
        for (Object pathObj : paths) {
            JSONObject pathJSON = (JSONObject) pathObj;
            Path path = Path.ofJSON(pathJSON);
            //DB 데이터랑 검증해서 setStep
            setStepListByJSON(pathJSON, path);
            pathList.add(path);
        }
        //TODO: filtering
//        pathList.forEach(System.out::println);
        return pathList;
    }
    
    private void setStepListByJSON(JSONObject pathJSON, Path path) {
        List<Step> stepList = new ArrayList<>();
        
        JSONArray legs = pathJSON.getJSONArray("legs");
        for (Object legObj : legs) {
            JSONObject leg = (JSONObject) legObj;
            setEachStep(stepList, leg);
        }
        path.setStepList(stepList);
    }
    
    private void setEachStep(List<Step> stepList, JSONObject leg) {
        JSONArray steps = leg.getJSONArray("steps");
        for (Object stepObj : steps) {
            JSONObject step = (JSONObject) stepObj;
            String type = (String) step.get("type");
            if (!type.equals("BUS") && !type.equals("WALKING"))
                continue;
            if (type.equals("BUS")) {
                setBusToStepList(stepList, step);
            }
            if (type.equals("WALKING")) {
                stepList.add(Step.ofJSON(step));
            }
        }
    }
    
    private void setBusToStepList(List<Step> stepList, JSONObject step) {
        Step ofJSON = Step.ofJSON(step);
        
        JSONObject departure = (JSONObject) step.getJSONArray("stations").get(0);
        JSONObject arrival = (JSONObject) step.getJSONArray("stations").get(1);
        
        String departureNodeNo = (String) departure.get("displayCode");
        String arrivalNodeNo = (String) arrival.get("displayCode");
        
        Node departureNode = getNode(departureNodeNo);
        Node arrivaNode = getNode(arrivalNodeNo);
        
        ofJSON.setDeparture(Objects.requireNonNull(departureNode).getNodeName());
        ofJSON.setArrival(Objects.requireNonNull(arrivaNode).getNodeName());
        
        //TODO: setArrivalRouteList
        ofJSON.setArrivalRouteList(setRandomArrivalRouteList(departure));
        stepList.add(ofJSON);
    }
    
    private List<ArrivalRoute> setRandomArrivalRouteList(JSONObject departure) {
        List<ArrivalRoute> arrivalRouteList = new ArrayList<>();
        Random random = new Random();
        List<Route> routes = routeRepository.findAll();
        for (int i = 0; i < Math.random() * 3 +1; i++) {
            ArrivalRoute arrivalRoute=new ArrivalRoute();
            
            Route route = routes.get(random.nextInt(routes.size() - 1));
            arrivalRoute.setDepartureName((String) departure.get("name"));
            arrivalRoute.setPrevNodeCnt((int) (Math.random() * 10 + 1));
            arrivalRoute.setArrTime((int) (Math.random() * 10 + 1));
            arrivalRoute.setRouteNo(route.getRouteNo());
            arrivalRoute.setRouteId(route.getRouteId());
            
            arrivalRouteList.add(arrivalRoute);
        }
        return arrivalRouteList;
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
            log.info("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            log.info("빠른길찾기 API 요청 결과: {}", sb);
            return new JSONObject(sb.toString());
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
    private String buildUrl(Double departureLongitude, Double departureLatitude, Double arrivalLongitude, Double arrivalLatitude) {
        String baseUrl = "https://pt.map.naver.com/api/pubtrans-search";
        return baseUrl + "?" +
                "&mode=TIME" +
                "&departureTime=" + LocalDateTime.now() +
                "&departure=" + departureLongitude + "," + departureLatitude +
                "&arrival=" + arrivalLongitude + "," + arrivalLatitude;
    }
    
    private ArrayList<Path> filterPath(List<ArrivalRoute> arrivalRouteList) {
        return null;
    }
}