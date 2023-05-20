package org.stupid_talking_potatoes.kis.service;

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
        // Logger 세팅 - info 로그만 출력
        LoggerContext logContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger log = logContext.getLogger("com.jayway.jsonpath.internal.path.CompiledPath");
        log.setLevel(Level.INFO);
        //test_json파일 가져와서 테스트
        res = new JSONObject(Files.readString(Paths.get("src/test/resources/test_json.json")));
        //------실시간 정보 테스트
//        NaverService naverService=new NaverService();
//        Double departureLongitude = 126.9743832;
//        Double departureLatitude = 37.5666103;
//        Double arrivalLongitude = 127.0093054;
//        Double arrivalLatitude = 37.5550384;
//        JSONObject result = naverService.getPtMapJSON(departureLongitude, departureLatitude, arrivalLongitude, arrivalLatitude);
        //-------
//        System.out.println(res);
        
    }
    @Test
    @DisplayName("JSON res 검증")
    void validateJson() {
        assertThat(json.from(res.toString()))
                .hasJsonPath("$.res");
    }
    
    @Nested
    @DisplayName("각 파라미터 검증")
    class ValidateJSON {
        
        private final BasicJsonTester json = new BasicJsonTester(getClass());
        
        @Test
        @DisplayName("res 검증")
        void validateEachPath() {
            JSONArray paths = res.getJSONObject("res").getJSONArray("paths");
            assertThat(paths).isNotNull();
            validatePaths(paths);
        }
    

        void validatePaths(JSONArray paths) {
            for (Object path : paths) {
                JSONObject pathObj = (JSONObject) path;
                assertThat(json.from(pathObj.toString()))
                        .isNotNull()
                        .hasJsonPath("$.departureTime");
                JSONArray legs = pathObj.getJSONArray("legs");
                validateLegs(legs);
            }
        }
        

        void validateLegs(JSONArray legs) {
            for (Object leg : legs) {
                JSONObject legObj = (JSONObject) leg;
                assertThat(json.from(legObj.toString()))
                        .isNotNull();
                JSONArray steps = legObj.getJSONArray("steps");
                validateSteps(steps);
            }
        }
    

        void validateSteps(JSONArray steps){
            for (Object step : steps) {
                JSONObject stepObj = (JSONObject) step;
                assertThat(json.from(stepObj.toString()))
                        .isNotNull()
                        .hasJsonPath("$.duration")
                        .hasJsonPath("$.type");
                String stepType = stepObj.optString("type", "");
                if (stepType.equals("BUS")) {
                    String arrivalTime = stepObj.get("arrivalTime").toString();
                    String departureTime = stepObj.get("departureTime").toString();
                    JSONObject routeObj = stepObj.getJSONObject("route");
                    assertThat(json.from(routeObj.toString()))
                            .isNotNull()
                            .hasJsonPath("$.name");
                    String routeName = routeObj.getString("name");
                    log.info("arrivalTime: {}",arrivalTime );
                    log.info("departureTime: {}",departureTime);
                    log.info("route name: {}", routeName);
            
                    JSONArray stations=stepObj.getJSONArray("stations");
                    validateStations(stations);
                }
            }
        }

        void validateStations(JSONArray stations) {
            for (Object station : stations) {
                JSONObject stationObj = (JSONObject) station;
                assertThat(json.from(stationObj.toString()))
                        .isNotNull()
                        .hasJsonPath("$.id")
                        .hasJsonPath("$.displayName")
                        .hasJsonPath("$.displayCode")
                        .hasJsonPath("$.name")
                        .hasJsonPath("$.duration");
                log.info("station name: {}", stationObj.get("name"));
            }
        }
    }
    
    
    
    @Test
    @DisplayName("DTO로 변환 테스트")
    void dataTransformTest() {
//        log.info(res.getJSONObject("res").toString());
//        log.info(res.getJSONObject("res").getJSONArray("paths").getJSONObject(0).get("departureTime").toString());
    }
}