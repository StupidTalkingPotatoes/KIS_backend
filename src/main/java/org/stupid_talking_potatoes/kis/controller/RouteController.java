package org.stupid_talking_potatoes.kis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stupid_talking_potatoes.kis.dto.route.RealtimeBusLocationInfo;
import org.stupid_talking_potatoes.kis.dto.route.SearchedRoute;
import org.stupid_talking_potatoes.kis.service.RouteService;

import java.util.ArrayList;

/**
 * package :  org.stupid_talking_potatoes.kis.route.controller
 * fileName : RouteController
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@RestController
public class RouteController {
    @Autowired
    RouteService routeService;
    
    @GetMapping()
    public RealtimeBusLocationInfo getRealtimeBusLocationInfo(String routeId){
        return null;
    }
    
    @GetMapping()
    public ArrayList<SearchedRoute> getRouteList(String routeNo){
        return null;
    }
}
