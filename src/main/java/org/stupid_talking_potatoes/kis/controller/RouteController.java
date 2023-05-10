package org.stupid_talking_potatoes.kis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stupid_talking_potatoes.kis.dto.route.RealtimeBusLocationInfo;
import org.stupid_talking_potatoes.kis.dto.route.SearchedRoute;
import org.stupid_talking_potatoes.kis.entity.Route;
import org.stupid_talking_potatoes.kis.service.RouteService;

import java.util.ArrayList;

/**
 * package :  org.stupid_talking_potatoes.kis.route.controller
 * fileName : RouteController
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@RestController
@RequestMapping("/routes")
public class RouteController {
    @Autowired
    RouteService routeService;
    
    @GetMapping("/location")
    public RealtimeBusLocationInfo getRealtimeBusLocationInfo(String routeId){
        return null;
    }
    
    @GetMapping("/arrive-info")
    public ArrayList<SearchedRoute> getRouteList(String routeNo){
        return null;
    }

    @GetMapping("/getRoute/{routeId}")
    public Route getRoute(@PathVariable String routeId) {
        return routeService.getRouteById(routeId);
    }
}
