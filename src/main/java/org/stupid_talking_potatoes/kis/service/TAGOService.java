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
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.stupid_talking_potatoes.kis.dto.node.PassingNode;
import org.stupid_talking_potatoes.kis.dto.route.ArrivalRoute;
import org.stupid_talking_potatoes.kis.dto.tago.*;
import org.stupid_talking_potatoes.kis.entity.Node;

import java.nio.charset.StandardCharsets;
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

    public ArrayList<TAGO_BusArrivalInfo> convertArrivalInfo(String body) {
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

    public List<ArrivalRoute> requestAndFilterBusArrivalInfo(UriComponents uri) {
        // request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri.toString(), String.class);

        // check status code
        if (response.getStatusCode() != HttpStatusCode.valueOf(200)) {
            throw new RuntimeException(); // TODO: Exception Handling
        }

        // convert from xml to object
        String responseXmlBody = response.getBody(); // get xml body
        JSONObject responseBody = XML.toJSONObject(responseXmlBody); // xml to json
        ArrayList<TAGO_BusArrivalInfo> arrivalInfoList = this.convertArrivalInfo(responseBody.toString()); // json to object

        // Filtering and return
        return this.filterBusArrivalInfo(arrivalInfoList);
    }

    public List<ArrivalRoute> requestRealtimeSpecificBusArrivalInfo(String nodeId, String routeId) {
        // set url
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("apis.data.go.kr")
                .path("/1613000/ArvlInfoInqireService/getSttnAcctoSpcifyRouteBusArvlPrearngeInfoList")
                .queryParam("serviceKey", this.serviceKey)
                // if there is error, return type is always xml.
                .queryParam("_type", "xml")
                .queryParam("cityCode", this.cityCode)
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 100)
                .queryParam("nodeId", nodeId)
                .queryParam("routeId", routeId)
                .build();
        return this.requestAndFilterBusArrivalInfo(uri);
    }

    public List<ArrivalRoute> requestRealtimeBusArrivalInfo(String nodeId) {
        // set url
        UriComponents uri = UriComponentsBuilder.newInstance()
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
        return this.requestAndFilterBusArrivalInfo(uri);
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

    public ArrayList<ArrivalRoute> filterBusArrivalInfo(ArrayList<TAGO_BusArrivalInfo> busArrivalInfoList) {
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
