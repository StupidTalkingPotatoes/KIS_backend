package org.stupid_talking_potatoes.kis.service.tago;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.stupid_talking_potatoes.kis.config.TagoServiceConfig;
import org.stupid_talking_potatoes.kis.exception.InternalServerException;
import org.stupid_talking_potatoes.kis.exception.ThirdPartyAPIException;

import java.nio.charset.StandardCharsets;

import java.util.Collections;
import java.util.List;

/**
 * TAGO API에 접근하는 Base 서비스
 * @param <T> TAGO API Response Dto 클래스
 * @param <U> API의 응답을 필터링한 후 반환되는 List 원소 클래스
 */
@Service
@Getter
@RequiredArgsConstructor
public abstract class TagoBaseService<T, U> {

    private String serviceKey;
    private Integer cityCode;

    protected TagoBaseService(TagoServiceConfig config) {
        this.serviceKey = config.getServiceKey();
        this.cityCode = config.getCityCode();
    }

    /**
     * 초 단위를 분 단위로 변경 (나머지는 절삭)
     * @param sec 초
     * @return minute
     */
    protected int convertSecToMin(int sec) {
        return Math.round((float)sec/60);
    }

    /**
     * url에 요청하고 status code를 검사한 후 응답의 xml object를 json object로 변경하여 반환
     * @param url 외부 api에 요청할 url
     * @return 요청에 대한 응답의 json body
     */
    protected JSONObject request(String url) {
        // request
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // check status code
        if (response.getStatusCode() != HttpStatusCode.valueOf(200)) {
            throw ThirdPartyAPIException.builder()
                    .message("TAGO API Error")
                    .content(response.getStatusCode().toString())
                    .build();
        }

        String responseXmlBody = response.getBody(); // get xml body
        return XML.toJSONObject(responseXmlBody); // xml to json
    }

    /**
     * Json body를 T가 담긴 리스트로 변환
     * @param body Json body
     * @param singleReference 바꿀 타입 레퍼런스 (T)
     * @param listReference 바꿀 타입 레퍼런스 (List<T>)
     * @return body에서 추출한 T 리스트
     */
    protected List<T> convert(String body, TypeReference<T> singleReference, TypeReference<List<T>> listReference) {
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

            // convert items to object list
            JsonNode items = responseBody.get("items");
            JsonNode item = items.get("item");
            JsonNodeType nodeType = item.getNodeType();

            if (nodeType.equals(JsonNodeType.OBJECT)) {
                T info = objectMapper.convertValue(item, singleReference);
                return Collections.singletonList(info);
            } else if (nodeType.equals(JsonNodeType.ARRAY)) {
                return objectMapper.convertValue(item, listReference);
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
     * TAGO API의 응답을 조건에 맞게 필터링한다.
     * API 별로 필터링할 조건이 다르기 때문에 상속받는 클래스에서 구현하게 강제한다.
     * @param list 조건에 맞게 필터링할 리스트
     * @return 필터링 결과
     */
    protected abstract List<U> filter(List<T> list);
}
