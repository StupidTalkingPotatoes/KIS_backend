package org.stupid_talking_potatoes.kis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.stupid_talking_potatoes.kis.dto.node.PassingNode;
import org.stupid_talking_potatoes.kis.dto.route.ArrivalRoute;
import org.stupid_talking_potatoes.kis.dto.tago.TAGO_AroundNodeInfo;
import org.stupid_talking_potatoes.kis.dto.tago.TAGO_BusArrivalInfo;
import org.stupid_talking_potatoes.kis.dto.tago.TAGO_BusLocationInfo;
import org.stupid_talking_potatoes.kis.entity.Node;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * package :  org.stupid_talking_potatoes.kis.tago.service
 * fileName : TAGOService
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Service
public class TAGOService {
    private final String serviceKey = "1XxfhSdKbDyiLDzEHz5mnkYKHAfpwM9SBibMSvTaXf4ybFVKHkQbzGUM1PSPWVTNKK5tG8T9oepg4NcTjgmjGA==";
    private final Integer cityCode = 37050; // Gumi City Code

    public int convertSecToMin(int min) {
        return Math.round((float)min/60);
    }

    public String encodeToUTF8(String rawString) {
        byte[] bytes = rawString.getBytes(StandardCharsets.ISO_8859_1);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public ArrayList<TAGO_AroundNodeInfo> convertAroundNodes(String body) {
        ObjectMapper objectMapper = new ObjectMapper()
                // fields of dto are camelCase, but fields of TAGO api are lowercase
                .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE);
        try {
            // json string to JsonNode
            JsonNode jsonNode = objectMapper.readTree(body);

            // get header -> check resultCode
            JsonNode responseHeader = jsonNode.get("response").get("header");
            if (!responseHeader.get("resultCode").asText().equals("00")) { // when there is error
                String errorMsg = responseHeader.get("resultMsg").asText();
                throw new RuntimeException(errorMsg); // TODO: Exception Handling
            }

            // get body -> check items count
            JsonNode responseBody = jsonNode.get("response").get("body");
            if (responseBody.get("totalCount").asInt() == 0) { // when items is empty
                return new ArrayList<>();
            }

            // convert items to object list & return
            ArrayNode arrayNode = (ArrayNode) responseBody.get("items").get("item");
            ArrayList<TAGO_AroundNodeInfo> aroundNodeList = objectMapper.convertValue(arrayNode, new TypeReference<ArrayList<TAGO_AroundNodeInfo>>() {});
            return aroundNodeList;

        } catch (JsonMappingException e) { // TODO: Exception Handling
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) { // TODO: Exception Handling
            throw new RuntimeException(e);
        }
    }

    public ArrayList<TAGO_BusArrivalInfo> convert(String body) {
        ObjectMapper objectMapper = new ObjectMapper()
                // fields of dto are camelCase, but fields of TAGO api are lowercase
                .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE);
        try {
            // json string to JsonNode
            JsonNode jsonNode = objectMapper.readTree(body);

            // get header -> check resultCode
            JsonNode responseHeader = jsonNode.get("response").get("header");
            if (!responseHeader.get("resultCode").asText().equals("00")) { // when there is error
                String errorMsg = responseHeader.get("resultMsg").asText();
                throw new RuntimeException(errorMsg); // TODO: Exception Handling
            }

            // get body -> check items count
            JsonNode responseBody = jsonNode.get("response").get("body");
            if (responseBody.get("totalCount").asInt() == 0) { // when items is empty
                return new ArrayList<>();
            }

            // convert items to object list & return
            ArrayNode arrayNode = (ArrayNode) responseBody.get("items").get("item");
            ArrayList<TAGO_BusArrivalInfo> busArrivalInfoList = objectMapper.convertValue(arrayNode, new TypeReference<ArrayList<TAGO_BusArrivalInfo>>() {});
            return busArrivalInfoList;

        } catch (JsonMappingException e) { // TODO: Exception Handling
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) { // TODO: Exception Handling
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ArrivalRoute> requestRealtimeBusArrivalInfo(String nodeId) {
        // set url
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("apis.data.go.kr")
                .path("/1613000/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList")
                .queryParam("serviceKey", this.serviceKey)
                // if there is error, return type is always xml.
                .queryParam("_type", "xml")
                .queryParam("cityCode", this.cityCode)
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 100)
                .queryParam("nodeId", nodeId)
                .build();

        // request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uriComponents.toString(), String.class);

        // check status code
        if (response.getStatusCode() != HttpStatusCode.valueOf(200)) {
            throw new RuntimeException(); // TODO: Exception Handling
        }

        // convert from xml to object
        String responseXmlBody = response.getBody(); // get xml body
        JSONObject responseBody = XML.toJSONObject(responseXmlBody); // xml to json
        ArrayList<TAGO_BusArrivalInfo> arrivalInfoList = this.convert(responseBody.toString()); // json to object

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
        // set url
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("apis.data.go.kr")
                .path("/1613000/BusSttnInfoInqireService/getCrdntPrxmtSttnList")
                .queryParam("serviceKey", this.serviceKey)
                // if there is error, return type is always xml.
                .queryParam("_type", "xml")
                .queryParam("pageNo", 1)
                .queryParam("numOfRows",100)
                .queryParam("gpsLong", longitude)
                .queryParam("gpsLati", latitude)
                .build();

        // request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uriComponents.toString(), String.class);

        // check status code
        if (response.getStatusCode() != HttpStatusCode.valueOf(200)) {
            throw new RuntimeException(); // TODO: Exception Handling
        }

        // convert from xml to object
        String responseXmlBody = response.getBody(); // get xml body
        JSONObject responseBody = XML.toJSONObject(responseXmlBody); // xml to json
        ArrayList<TAGO_AroundNodeInfo> aroundNodeList = this.convertAroundNodes(responseBody.toString()); // json to object

        // filter and map
        ArrayList<Node> nodeList = this.filterArroundNodeInfo(aroundNodeList);
        return nodeList;
    }

    public ArrayList<ArrivalRoute> filterBusArrivalInfo(ArrayList<TAGO_BusArrivalInfo> busArrivalInfoList){
        ArrayList<ArrivalRoute> arrivalRoutes = new ArrayList<>();
        for (TAGO_BusArrivalInfo busArrivalInfo: busArrivalInfoList) {
            // encode vehicleTp to utf-8
            String encodedVehicleTp = this.encodeToUTF8(busArrivalInfo.getVehicleTp());

            // check kneeling bus
            if (encodedVehicleTp.equals("저상버스")) {
                // convert time from sec to min
                int arrTimeSec = busArrivalInfo.getArrTime();
                int arrTimeMin = this.convertSecToMin(arrTimeSec);

                // build object and add to list
                arrivalRoutes.add(
                        ArrivalRoute.builder()
                                .routeId(busArrivalInfo.getRouteId())
                                .routeNo(busArrivalInfo.getRouteNo())
                                .prevNodeCnt(busArrivalInfo.getArrPrevStationCnt())
                                .arrTime(arrTimeMin)
                                .build()
                );
            }
        }
        return arrivalRoutes;
    }

    public ArrayList<ArrivalRoute> filterBusLocationInfo(ArrayList<TAGO_BusLocationInfo> busLocationInfoList){
        return null;
    }

    public ArrayList<Node> filterArroundNodeInfo(ArrayList<TAGO_AroundNodeInfo> aroundNodeList) {
        ArrayList<Node> nodeList = new ArrayList<>();
        for (TAGO_AroundNodeInfo arrivalInfo: aroundNodeList) {
            // filter by city code
            if (arrivalInfo.getCityCode().equals(this.cityCode)) {
                // encode name to utf-8
                arrivalInfo.setNodeNm(this.encodeToUTF8(arrivalInfo.getNodeNm()));
                nodeList.add(Node.of(arrivalInfo));
            }
        }
        return nodeList;
    }
}
