package org.stupid_talking_potatoes.kis.naver.service;

import org.springframework.stereotype.Service;
import org.stupid_talking_potatoes.kis.path.dto.Path;
import org.stupid_talking_potatoes.kis.route.domain.ArrivalRoute;

import java.util.ArrayList;
import java.util.List;

/**
 * package :  org.stupid_talking_potatoes.kis.naver.service
 * fileName : NaverService
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Service
public class NaverService {
    public ArrayList<Path> getTransportationInfo(Double departureLongitude,Double departureLatitude,Double arrivalLongitude,Double arrivalLatitude){
        return  null;
    }
    private  ArrayList<Path> filterPath(List<ArrivalRoute> arrivalRouteList){
        return null;
    }
}
