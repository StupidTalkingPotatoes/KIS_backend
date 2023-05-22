package org.stupid_talking_potatoes.kis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.stupid_talking_potatoes.kis.dto.route.RealtimeBusLocationInfo;
import org.stupid_talking_potatoes.kis.dto.route.SearchedRoute;
import org.stupid_talking_potatoes.kis.entity.Route;
import org.stupid_talking_potatoes.kis.repository.RouteRepository;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * package :  org.stupid_talking_potatoes.kis.route.service
 * fileName : RouteService
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;

    public RealtimeBusLocationInfo getRealtimeBusLocationInfo(String routeId){
        return null;
    }
    
    public String getArrivalName(String routeId){
        Route route = routeRepository.findByRouteId(routeId).orElseThrow(()-> new NoSuchElementException());
        return route.getEndNodeName();
    }
    
    public ArrayList<SearchedRoute> getRouteList(String routeNo){
        return null;
    }
    
}
