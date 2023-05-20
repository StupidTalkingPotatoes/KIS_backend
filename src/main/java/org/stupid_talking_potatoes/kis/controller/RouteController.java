package org.stupid_talking_potatoes.kis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stupid_talking_potatoes.kis.dto.route.RealtimeBusLocationInfo;
import org.stupid_talking_potatoes.kis.dto.route.SearchedRoute;
import org.stupid_talking_potatoes.kis.entity.Route;
import org.stupid_talking_potatoes.kis.service.RouteService;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * package :  org.stupid_talking_potatoes.kis.route.controller
 * fileName : RouteController
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RouteController {
    private final RouteService routeService;

    @GetMapping("/location")
    public RealtimeBusLocationInfo getRealtimeBusLocationInfo(String routeId) throws UnsupportedEncodingException {
        return routeService.getBusRouteInfo(routeId);
    }
    
    @GetMapping("/arrive-info")
    public ArrayList<SearchedRoute> getRouteList(String routeNo){
        return null;
    }
}
