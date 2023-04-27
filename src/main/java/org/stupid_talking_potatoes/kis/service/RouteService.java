package org.stupid_talking_potatoes.kis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stupid_talking_potatoes.kis.dto.path.repository.RouteRepository;
import org.stupid_talking_potatoes.kis.dto.route.RealtimeBusLocationInfo;
import org.stupid_talking_potatoes.kis.dto.route.SearchedRoute;
import org.stupid_talking_potatoes.kis.entity.Route;

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
    public RealtimeBusLocationInfo getRealtimeBusLocationInfo(String routeId){
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
