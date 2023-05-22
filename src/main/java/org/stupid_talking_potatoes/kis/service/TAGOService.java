package org.stupid_talking_potatoes.kis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.stupid_talking_potatoes.kis.dto.node.PassingNode;
import org.stupid_talking_potatoes.kis.dto.route.ArrivalRoute;
import org.stupid_talking_potatoes.kis.dto.tago.*;
import org.stupid_talking_potatoes.kis.entity.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * package :  org.stupid_talking_potatoes.kis.tago.service
 * fileName : TAGOService
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Service
@RequiredArgsConstructor
public class TAGOService {
    private final String serviceKey = "1XxfhSdKbDyiLDzEHz5mnkYKHAfpwM9SBibMSvTaXf4ybFVKHkQbzGUM1PSPWVTNKK5tG8T9oepg4NcTjgmjGA==";
    private final String cityCode = "37050"; // Gumi City Code

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
            ArrayNode arrayNode = (ArrayNode) responseBody.get("items");
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
                .queryParam("pageNo", String.valueOf(1))
                .queryParam("numOfRows", String.valueOf(100))
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
        JSONObject responseBody = XML.toJSONObject(response.getBody()); // xml to json
        ArrayList<TAGO_BusArrivalInfo> arrivalInfoList = this.convert(responseBody.toString()); // json to object

        // Filtering and return
        return this.filterBusArrivalInfo(arrivalInfoList);
    }

    public ArrayList<ArrivalRoute> requestRealtimeSpecificBusLocationInfo(String nodeId, String routeId) {
        return null;
    }

    public ArrayList<PassingNode> requestRealtimeBusLocationInfo(String nodeId) {
        return null;
    }

    public ArrayList<Node> requestAroundNodeInfo(Double longitude, Double latitude) {
        return null;
    }

    public ArrayList<ArrivalRoute> filterBusArrivalInfo(ArrayList<TAGO_BusArrivalInfo> busArrivalInfoList) {
        ArrayList<ArrivalRoute> arrivalRoutes = new ArrayList<>();
        for (TAGO_BusArrivalInfo busArrivalInfo : busArrivalInfoList) {
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

    public ArrayList<ArrivalRoute> filterBusLocationInfo(ArrayList<TAGO_BusLocationInfo> busLocationInfoList) {
        return null;
    }

    /**
     * routeId에 해당하는 경로를 지나는 nodeList를 API로 조회
     * @param routeId routeId
     * @return 경로를 지나는 nodeList
     */
    public List<PassingNode> getPassingNodeList(String routeId) {

        ResponseEntity<TAGO_BusLocationInfoResponse> responseEntity = requestBusLocationInfo(routeId);

        if (responseEntity.getStatusCode() != HttpStatusCode.valueOf(200)) {
            throw new RuntimeException();
        }

        TAGO_BusLocationInfoResponse responseBody = responseEntity.getBody();
        TAGO_ApiResponse<TAGO_BusLocationInfo> TAGOApiResponse = Objects.requireNonNull(responseBody).getResponse();
        TAGO_ApiResponse.Header header = TAGOApiResponse.getHeader();

        if (!header.getResultCode().equals("00")) {
            String resultMsg = header.getResultMsg();
            throw new RuntimeException(resultMsg);
        }

        TAGO_ApiResponse.Body<TAGO_BusLocationInfo> body = TAGOApiResponse.getBody();
        TAGO_ApiResponse.Body.Items<TAGO_BusLocationInfo> items = body.getItems();
        List<TAGO_BusLocationInfo> item = items.getItem();

        List<PassingNode> passingNodeList = new ArrayList<>();

        for (TAGO_BusLocationInfo busLocationInfo : item) {
            passingNodeList.add(PassingNode.from(busLocationInfo));
        }

        return passingNodeList;
    }

    /**
     * nodeList 조회 API
     * @param routeId routeId
     * @return TAGO_BusLocationInfoResponse
     */
    private ResponseEntity<TAGO_BusLocationInfoResponse> requestBusLocationInfo(String routeId) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("apis.data.go.kr")
                .path("/1613000/BusRouteInfoInqireService/getRouteAcctoThrghSttnList")
                .queryParam("serviceKey", this.serviceKey)
                .queryParam("_type", "json")
                .queryParam("cityCode", this.cityCode)
                .queryParam("pageNo", String.valueOf(1))
                .queryParam("numOfRows", String.valueOf(100))
                .queryParam("routeId", routeId)
                .build();

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(uriComponents.toString(), TAGO_BusLocationInfoResponse.class);
    }

    /**
     * RealTimeBusLocationInfo를 API로 조회
     * @param routeId routeId
     * @return List<realTimeBusLocationInfo>
     */
    public List<TAGO_RealTimeBusLocationInfo> requestRealTimeBusLocationInfo(String routeId) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("apis.data.go.kr")
                .path("/1613000/BusLcInfoInqireService/getRouteAcctoBusLcList")
                .queryParam("serviceKey", this.serviceKey)
                .queryParam("_type", "json")
                .queryParam("cityCode", this.cityCode)
                .queryParam("routeId", routeId)
                .build();
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uriComponents.toString(), String.class);
        if (responseEntity.getStatusCode() != HttpStatusCode.valueOf(200)) {
            throw new RuntimeException();
        }

        return convertRealTimeBusLocationInfo(responseEntity.getBody());
    }

    /**
     * API로 받아온 Json String을 realTimeBusLocationInfo로 파싱하여 리스트로 반환
     * @param body body
     * @return List<realTimeBusLocationInfo>
     */
    public List<TAGO_RealTimeBusLocationInfo> convertRealTimeBusLocationInfo(String body) {
        ObjectMapper objectMapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE);
        try {
            JsonNode jsonNode = objectMapper.readTree(body);

            JsonNode responseHeader = jsonNode.get("response").get("header");
            if (!responseHeader.get("resultCode").asText().equals("00")) { // when there is error
                String errorMsg = responseHeader.get("resultMsg").asText();
                throw new RuntimeException(errorMsg); // TODO: Exception Handling
            }

            JsonNode responseBody = jsonNode.get("response").get("body");
            if (responseBody.get("totalCount").asInt() == 0) { // when items is empty
                return Collections.emptyList();
            }

            JsonNode items = responseBody.get("items");
            JsonNode item = items.get("item");
            JsonNodeType nodeType = item.getNodeType();

            if (nodeType.equals(JsonNodeType.OBJECT)) {
                TAGO_RealTimeBusLocationInfo realTimeBusLocationInfo = objectMapper.convertValue(item, TAGO_RealTimeBusLocationInfo.class);
                return Collections.singletonList(realTimeBusLocationInfo);
            } else if (nodeType.equals(JsonNodeType.ARRAY)) {
                return objectMapper.convertValue(item, new TypeReference<>(){});
            }

            return Collections.emptyList();

        } catch (JsonProcessingException e) { // TODO: Exception Handling
            throw new RuntimeException(e);
        }
    }
}
