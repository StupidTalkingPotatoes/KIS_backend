package org.stupid_talking_potatoes.kis.service.tago;

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
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.stupid_talking_potatoes.kis.config.TagoServiceConfig;
import org.stupid_talking_potatoes.kis.dto.tago.TAGO_RealTimeBusLocationInfo;
import org.stupid_talking_potatoes.kis.entity.Node;
import org.stupid_talking_potatoes.kis.exception.InternalServerException;
import org.stupid_talking_potatoes.kis.exception.ThirdPartyAPIException;
import org.stupid_talking_potatoes.kis.service.KneelingBusService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RealTimeBusLocationInfoService extends TagoBaseService<TAGO_RealTimeBusLocationInfo, Integer> {
    private KneelingBusService kneelingBusService;

    public RealTimeBusLocationInfoService(TagoServiceConfig config, KneelingBusService kneelingBusService) {
        super(config);
        this.kneelingBusService = kneelingBusService;
    }

    private String buildUri(String routeId) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("apis.data.go.kr")
                .path("/1613000/BusLcInfoInqireService/getRouteAcctoBusLcList")
                .queryParam("serviceKey", super.getServiceKey())
                .queryParam("_type", "xml")
                .queryParam("cityCode", super.getServiceKey())
                .queryParam("routeId", routeId)
                .build();
        return uriComponents.toString();
    }

    public List<Integer> getRealtimeNodeOrderList(String routeId) {
        // URL 만들기
        String url = this.buildUri(routeId);
        // TAGO API 요청하여 JSON 응답 받아오기
        JSONObject responseBody = super.request(url);
        // 내가 원하는 객체리스트로 convert하기
        List<TAGO_RealTimeBusLocationInfo> realTimeBusLocationInfoList =
                convert(responseBody.toString(), new TypeReference<>(){}, new TypeReference<>(){});
        // 현재 저상버스 실시간 위치 반환
        return filter(realTimeBusLocationInfoList);
    }

    protected List<TAGO_RealTimeBusLocationInfo> convertTemp(String body, TypeReference<ArrayList<TAGO_RealTimeBusLocationInfo>> typeReference) {
        ObjectMapper objectMapper = new ObjectMapper()
                // fields of dto are camelCase, but fields of TAGO api are lowercase
                .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE);
        try {
            // json string to JsonNode
            JsonNode jsonNode = objectMapper.readTree(body);

            // get header -> check resultCode
            JsonNode responseHeader = jsonNode.get("response").get("header");
            if (!responseHeader.get("resultCode").asText().equals("00")) { // when there is error
                throw ThirdPartyAPIException.builder()
                        .message("TAGO API Error")
                        .content(responseHeader.get("resultMsg").asText())
                        .build();
            }

            // get body -> check items count
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
                return objectMapper.convertValue(item, new TypeReference<>() {
                });
            }

            return Collections.emptyList();

        } catch (JsonMappingException e) {
            throw InternalServerException.builder()
                    .message("Json Mapping Exception")
                    .content(e.getMessage())
                    .build();
        } catch (JsonProcessingException e) {
            throw InternalServerException.builder()
                    .message("Json Processing Exception")
                    .content(e.getMessage())
                    .build();
        } catch (NullPointerException e) {
            throw InternalServerException.builder()
                    .message("Null Pointer Exception")
                    .content(e.getMessage())
                    .build();
        }
    }
    /**
     * 현재 운행 중인 저상버스 필터링
     * @param realTimeBusLocationInfoList realTimeBusLocationInfoList(현재 운행 중인 버스 정보)
     * @return realtimeNodeOrderList(현재 운행 중인 저상버스 위치 리스트)
     */
    @Override
    protected List<Integer> filter(List<TAGO_RealTimeBusLocationInfo> realTimeBusLocationInfoList) {
        return realTimeBusLocationInfoList.stream()
                .filter(realTimeBusLocationInfo -> kneelingBusService.isKneelingBus(realTimeBusLocationInfo.getVehicleNo()))
                .map(TAGO_RealTimeBusLocationInfo::getNodeOrd)
                .toList();
    }

}
