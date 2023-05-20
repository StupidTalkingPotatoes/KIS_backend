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
import org.stupid_talking_potatoes.kis.entity.RouteSeq;
import org.stupid_talking_potatoes.kis.repository.NodeRepository;
import org.stupid_talking_potatoes.kis.repository.RouteRepository;
import org.stupid_talking_potatoes.kis.repository.RouteSeqRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        setPathProperty(paths, pathList);
//        pathList.forEach(System.out::println);
        return pathList;
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
            log.info("빠른길찾기 API 요청 결과: {}",sb);
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
    
    /**
     * 가져온 JSON의 path에 해당하는 속성을 하나씩 DTO로 매핑
     *
     * @param paths    paths
     * @param pathDtos pathDtos
     */
    void setPathProperty(JSONArray paths, ArrayList<Path> pathDtos) {
        for (Object pathObj : paths) {
            JSONObject path = (JSONObject) pathObj;
            pathDtos.add(new Path());
            Path pathDto = pathDtos.get(pathDtos.size() - 1);
            pathDto.setDuration(Integer.parseInt(path.get("duration").toString()));
            
            JSONArray legs = path.getJSONArray("legs");
            appendLegs(legs, pathDto.getStepList());
        }
    }
    
    void appendLegs(JSONArray legs, ArrayList<Step> stepList) {
        for (Object leg : legs) {
            JSONObject legObj = (JSONObject) leg;
            JSONArray steps = legObj.getJSONArray("steps");
            setStepProperties(steps, stepList);
        }
    }
    
    
    void setStepProperties(JSONArray steps, ArrayList<Step> stepList) {
        for (Object stepObj : steps) {
            JSONObject step = (JSONObject) stepObj;
            String stepType = step.get("type").toString();
            
            if (stepType.equals("BUS")) {
                stepList.add(new Step());
                Integer duration = Integer.parseInt(step.get("duration").toString());
                String departureTime = step.get("departureTime").toString();
                String arrivalTime = step.get("arrivalTime").toString();
                Step busStep = stepList.get(stepList.size() - 1);
                busStep.setType(stepType);
                busStep.setDuration(duration);
                busStep.setArrival(arrivalTime);
                busStep.setDeparture(departureTime);
                //dto로 저장
                
                JSONArray stations = step.getJSONArray("stations");//해당 step에서 승차 정류장, 하차 정류장 정보들을 가져옴
    
                String routeNo = (String) step.getJSONObject("route").get("name");
                List<Route> routeList = routeRepository.findByRouteNo(routeNo);//한개(?) 또는 두개
                /**
                 * 버스 번호로 찾게되면 방면때문에 두개가 나올건데,
                 * 이떄 RouteSeq를 가져온 Route를 통해 직접 확인하고,
                 * 확인된 RouteSeq의 현재 순서의 nodeOrder가 도착지의
                 * 순서의 nodeOrder보다 작으면 그게 원하는 Route 객체다
                 *  */
                for (Object stationObj:stations) {//출발 정류장 + 도착 정류장
                    JSONObject station = (JSONObject)stationObj;
                    Node nodeByNodeNo = nodeRepository.findByNodeNo((String) station.get("displayCode")).orElse(null);
                    log.info("정류장 ID:{}", Objects.requireNonNull(nodeByNodeNo).getNodeId());
                    
                    for (Route route : routeList) {
                        RouteSeq seq = routeSeqRepository.findByRoute_RouteIdAndNode_NodeId(route.getRouteId(), nodeByNodeNo.getNodeId());
                        if(seq==null)
                            continue;
                        log.info("노선 ID:{}",seq.getRoute().getRouteId());
                        log.info("순서:{}",seq.getNodeOrder());
                        //requsetRealtimeSpecificBusArrivalInfo메서드가 들어갈 자리
                    }
                }
            }
        }
    }
    
    void setArrivalRouteProperties(JSONArray stations, ArrayList<ArrivalRoute> arrivalRouteList) {
        JSONObject departureStation = (JSONObject) stations.get(1);
        arrivalRouteList.add(new ArrivalRoute());
        //TODO: ArrivalRoute 받아와서 Path 마무리짓기
        ArrivalRoute arrivalRoute = arrivalRouteList.get(arrivalRouteList.size() - 1);
        //더미 데이터 생성
//        Random random = new Random();
//        List<Route> routes = routeRepository.findAll();
//        Route route = routes.get(random.nextInt(routes.size() - 1));
//        arrivalRoute.setDepartureName((String) departureStation.get("name"));
//        arrivalRoute.setPrevNodeCnt((int) (Math.random()*10+1));
//        arrivalRoute.setArrTime((int) (Math.random()*10+1));
//        arrivalRoute.setRouteNo(route.getRouteNo());
//        arrivalRoute.setRouteId(route.getRouteId());
        //
        arrivalRouteList.add(arrivalRoute);
    }
    
    private ArrayList<Path> filterPath(List<ArrivalRoute> arrivalRouteList) {
        return null;
    }
}
