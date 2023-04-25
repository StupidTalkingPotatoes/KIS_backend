package org.stupid_talking_potatoes.kis.naver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.stupid_talking_potatoes.kis.path.dto.Path;
import org.stupid_talking_potatoes.kis.route.domain.ArrivalRoute;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * package :  org.stupid_talking_potatoes.kis.naver.service
 * fileName : NaverService
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Service
@Slf4j
public class NaverService {
    public ArrayList<Path> getTransportationInfo(Double departureLongitude,Double departureLatitude,Double arrivalLongitude,Double arrivalLatitude){
        return null;
    }
    private String buildUrl(Double departureLongitude, Double departureLatitude, Double arrivalLongitude, Double arrivalLatitude) {
        String baseUrl = "https://pt.map.naver.com/api/pubtrans-search";
        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        urlBuilder.append("?");
        urlBuilder.append("&mode=TIME");
        urlBuilder.append("&departureTime=").append(LocalDateTime.now());
        urlBuilder.append("&departure=").append(departureLongitude).append(",").append(departureLatitude);
        urlBuilder.append("&arrival=").append(arrivalLongitude).append(",").append(arrivalLatitude);
        return urlBuilder.toString();
    }

    private  ArrayList<Path> filterPath(List<ArrivalRoute> arrivalRouteList){
        return null;
    }
}
