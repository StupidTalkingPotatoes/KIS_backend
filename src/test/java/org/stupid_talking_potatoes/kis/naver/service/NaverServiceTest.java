package org.stupid_talking_potatoes.kis.naver.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.BasicJsonTester;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * package :  org.stupid_talking_potatoes.kis.naver.service
 * fileName : NaverServiceTest
 * author :  ShinYeaChan
 * date : 2023-04-25
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
class NaverServiceTest {
    private final NaverService naverService = new NaverService();
    private final BasicJsonTester json = new BasicJsonTester(getClass());
    JSONObject res;
    
    @BeforeEach
    @DisplayName("빠른 길찾기 api 연결")
    void init() throws IOException {
        res = new JSONObject(Files.readString(Paths.get("src/test/resources/test_json.json")));
        System.out.println(res.getJSONObject("res").getJSONArray("paths"));
    }
    @Test
    @DisplayName("JSON res 검증")
    void validateJson() {
        assertThat(json.from(res.toString()))
                .hasJsonPath("$.res");
    }
    
    @Nested
    @DisplayName("각 파라미터 검증")
    class validatePaths{
        JSONArray paths = res.getJSONObject("res").getJSONArray("paths");
        
        @Test
        @DisplayName("paths 검증")
        void validateEachPath() {
            System.out.println(paths.toString());
            assertThat(paths)
                    .isNotNull();
            for (Object path : paths) {
                JSONObject pathObj = (JSONObject) path;
                assertThat(json.from(pathObj.toString()))
                        .isNotNull()
                        .hasJsonPath("$.departureTime");
                JSONArray legs = ((JSONObject) path).getJSONArray("legs");
                for (Object leg:legs) {
                    JSONObject legObj=(JSONObject) leg;
                    assertThat(json.from(legObj.toString()))
                            .isNotNull()
                            .hasJsonPath("$.steps");
                }
            }
        }
    }
   
    
    @Test
    @DisplayName("DTO로 변환 테스트")
    void dataTransformTest() {
//        log.info(res.getJSONObject("res").toString());
        log.info(res.getJSONObject("res").getJSONArray("paths").getJSONObject(0).get("departureTime").toString());
    }
}