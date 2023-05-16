package org.stupid_talking_potatoes.kis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stupid_talking_potatoes.kis.dto.path.Path;

import java.util.ArrayList;

/**
 * package :  org.stupid_talking_potatoes.kis.path.service
 * fileName : PathService
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Service
public class PathService {
    @Autowired
    private NaverService naverService;
    public ArrayList<Path> getPathInfo(Double departureLongitude, Double departureLatitude, Double arrivalLongitude, Double arrivalLatitude){
        return naverService.getTransportationInfo(departureLongitude,departureLatitude,arrivalLongitude,arrivalLatitude);
    }
}
