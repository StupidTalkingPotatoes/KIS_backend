package org.stupid_talking_potatoes.kis.service.tago;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.stupid_talking_potatoes.kis.exception.InternalServerException;
import org.stupid_talking_potatoes.kis.exception.ThirdPartyAPIException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * TAGO API에 접근하는 Base 서비스
 * @param <T> TAGO API Response Dto 클래스
 * @param <U> API의 응답을 필터링한 후 반환되는 List 원소 클래스
 */
@Service
@RequiredArgsConstructor
public abstract class TagoBaseService<T, U> {

    protected final String serviceKey = "1XxfhSdKbDyiLDzEHz5mnkYKHAfpwM9SBibMSvTaXf4ybFVKHkQbzGUM1PSPWVTNKK5tG8T9oepg4NcTjgmjGA==";
    protected final Integer cityCode = 37050; // Gumi City Code

    protected String encodeToUTF8(String rawString) {
        byte[] bytes = rawString.getBytes(StandardCharsets.ISO_8859_1);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    protected int convertSecToMin(int min) {
        return Math.round((float)min/60);
    }

    protected JSONObject request(String url) {
        // request
        RestTemplate restTemplate = new RestTemplate();
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

    protected ArrayList<T> convert(String body, TypeReference<ArrayList<T>> typeReference) {
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
                return new ArrayList<>();
            }

            // convert items to object list & return
            ArrayNode arrayNode = (ArrayNode) responseBody.get("items").get("item");

            ArrayList<T> aroundNodeList = objectMapper.convertValue(arrayNode, typeReference);
            return aroundNodeList;

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

    protected abstract ArrayList<U> filter(ArrayList<T> list);
}
