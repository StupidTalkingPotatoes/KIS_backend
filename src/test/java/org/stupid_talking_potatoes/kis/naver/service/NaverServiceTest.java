package org.stupid_talking_potatoes.kis.naver.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * package :  org.stupid_talking_potatoes.kis.naver.service
 * fileName : NaverServiceTest
 * author :  ShinYeaChan
 * date : 2023-04-25
 */
@Slf4j
class NaverServiceTest {
    private final NaverService naverService = new NaverService();
    
    @Test
    @DisplayName("getPtMap 테스트")
    void getPtMapTest() {
        Double departureLongitude = 126.9743832;
        Double departureLatitude = 37.5666103;
        Double arrivalLongitude = 127.0093054;
        Double arrivalLatitude = 37.5550384;
        String result = naverService.getPtMap(departureLongitude, departureLatitude, arrivalLongitude, arrivalLatitude);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        log.info(result);
    }
}