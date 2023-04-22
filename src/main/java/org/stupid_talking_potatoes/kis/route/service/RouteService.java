package org.stupid_talking_potatoes.kis.route.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stupid_talking_potatoes.kis.route.domain.Route;
import org.stupid_talking_potatoes.kis.route.domain.SearchedRoute;
import org.stupid_talking_potatoes.kis.route.dto.RealtimeBusLocationResponse;
import org.stupid_talking_potatoes.kis.route.repository.RouteRepository;

import java.util.ArrayList;

/**
 * package :  org.stupid_talking_potatoes.kis.route.service
 * fileName : RouteService
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Service
public class RouteService {
    @Autowired
    RouteRepository routeRepository;
    public RealtimeBusLocationResponse getRealtimeBusLocationInfo(String routeId){
        return null;
    }
    
    public String getDeparture(String routeId){
        return null;
    }
    
    public ArrayList<SearchedRoute> getRouteList(String routeNo){
        return null;
    }
    
    public Route getRouteByNaverId(String naverRouteId){
        return null;
    }
}
