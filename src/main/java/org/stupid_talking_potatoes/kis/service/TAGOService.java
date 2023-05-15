package org.stupid_talking_potatoes.kis.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.stupid_talking_potatoes.kis.dto.node.PassingNode;
import org.stupid_talking_potatoes.kis.dto.route.ArrivalRoute;
import org.stupid_talking_potatoes.kis.dto.tago.TAGO_BusArrivalInfo;
import org.stupid_talking_potatoes.kis.dto.tago.TAGO_BusLocationInfo;
import org.stupid_talking_potatoes.kis.entity.Node;

import java.util.ArrayList;

/**
 * package :  org.stupid_talking_potatoes.kis.tago.service
 * fileName : TAGOService
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Service
public class TAGOService {
    private final String serviceKey = "AIRQ9bShsrEcDSlIAdtllvWroR+z6Ag67WYIXH8OF+rV+2ttq0l+hjQ8nLcDhA9aau7eEsKoP11dqoA2CrEIng==";
    private final String baseUrl = "http://apis.data.go.kr/1613000";
    private final String cityCode = "37050"; // Gumi City Code

    public ArrayList<TAGO_BusArrivalInfo> convert(String body) { // 테스트용
        return new ArrayList<>();
    }

    public ArrayList<ArrivalRoute> requestRealtimeBusArrivalInfo(String nodeId) {
        // set path params
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("serviceKey", this.serviceKey);
        params.add("_type", "json");
        params.add("cityCode", this.cityCode);
        params.add("nodeId", nodeId);

        // request
        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<TAGO_BusArrivalInfoResponse> response = restTemplate.getForEntity(baseUrl + "/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList", TAGO_BusArrivalInfoResponse.class, params);
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList", String.class, params);

        // Convert TAGO_BusArrivalInfoResponse to ArrayList<TAGO_BusArrivalInfo>
        ArrayList<TAGO_BusArrivalInfo> arrivalInfoList = this.convert(response.getBody());

        // Filtering and return
        return this.filterBusArrivalInfo(arrivalInfoList);
    }

    public ArrayList<ArrivalRoute> requestRealtimeSpecificBusLocationInfo(String nodeId, String routeId){
        return null;
    }

    public ArrayList<PassingNode> requestRealtimeBusLocationInfo(String nodeId){
        return null;
    }

    public ArrayList<Node> requestAroundNodeInfo(Double longitude, Double latitude){
        return null;
    }

    public ArrayList<ArrivalRoute> filterBusArrivalInfo(ArrayList<TAGO_BusArrivalInfo> busArrivalInfoList){
        ArrayList<ArrivalRoute> arrivalRoutes = new ArrayList<>();
        for (TAGO_BusArrivalInfo busArrivalInfo: busArrivalInfoList) {
            if (busArrivalInfo.getVehicleTp().equals("저상버스")) {
                arrivalRoutes.add(
                        ArrivalRoute.builder()
                                .routeId(busArrivalInfo.getRouteId())
                                .routeNo(busArrivalInfo.getRouteNo())
                                .prevNodeCnt(busArrivalInfo.getArrPrevStationCnt())
                                .arrTime(busArrivalInfo.getArrTime())
                                .build()
                );
            }
        }
        return arrivalRoutes;
    }

    public ArrayList<ArrivalRoute> filterBusLocationInfo(ArrayList<TAGO_BusLocationInfo> busLocationInfoList){
        return null;
    }
}
