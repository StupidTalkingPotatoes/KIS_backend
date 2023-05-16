package org.stupid_talking_potatoes.kis.naver.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
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
    private final BasicJsonTester json = new BasicJsonTester(getClass());
    JSONObject res;
    
    @BeforeEach
    @DisplayName("빠른 길찾기 api 연결")
    void init() throws IOException {
        LoggerContext logContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger log = logContext.getLogger("com.jayway.jsonpath.internal.path.CompiledPath");
        log.setLevel(Level.INFO);
        res = new JSONObject(Files.readString(Paths.get("src/test/resources/test_json.json")));
//        System.out.println(res.getJSONObject("res").getJSONArray("paths"));
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
        
        @Test
        @DisplayName("paths 검증")
        void validateEachPath() {
            JSONArray paths = res.getJSONObject("res").getJSONArray("paths");
//            System.out.println(paths.toString());
            assertThat(paths)
                    .isNotNull();
            for (Object pathObj : paths) {
                JSONObject path = (JSONObject) pathObj;
                assertThat(json.from(path.toString()))
                        .isNotNull()
                        .hasJsonPath("$.departureTime");
                JSONArray legs = path.getJSONArray("legs");
                for (Object legObj:legs) {
                    JSONObject leg=(JSONObject) legObj;
                    assertThat(json.from(leg.toString()))
                            .isNotNull()
                            .hasJsonPath("$.steps");
                    JSONArray steps = leg.getJSONArray("steps");
                    for (Object stepObj : steps) {
                        JSONObject step=(JSONObject)stepObj;
                        assertThat(json.from(step.toString()))
                                .isNotNull()
                                .hasJsonPath("$.duration")
                                .hasJsonPath("$.type");
                        if(step.get("type").toString().equals("BUS")){
                            JSONObject route = step.getJSONObject("route");
                            log.info((String) route.get("name"));
                            JSONArray stations = step.getJSONArray("stations");
    
                            JSONObject startNode = (JSONObject) stations.get(0);
                            JSONObject endNode = (JSONObject) stations.get(1);
                            log.info((String) startNode.get("name"));
                            log.info((String) endNode.get("name"));
                        }
                    }
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