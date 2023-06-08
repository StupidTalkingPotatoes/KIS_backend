package org.stupid_talking_potatoes.kis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stupid_talking_potatoes.kis.dto.route.RealtimeBusLocationInfo;
import org.stupid_talking_potatoes.kis.dto.route.SearchedRoute;
import org.stupid_talking_potatoes.kis.service.RouteService;

import java.util.List;

/**
 * package :  org.stupid_talking_potatoes.kis.route.controller
 * fileName : RouteController
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {
    private final RouteService routeService;
    @GetMapping("/search")
    public List<SearchedRoute> getRouteList(String routeNo){
        return routeService.getRouteList(routeNo);
    }
    @GetMapping("/location")
    public RealtimeBusLocationInfo getRealtimeBusLocationInfo(String routeId) {
        return routeService.getRealtimeBusLocationInfo(routeId);
    }
}
