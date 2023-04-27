package org.stupid_talking_potatoes.kis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stupid_talking_potatoes.kis.dto.path.Path;
import org.stupid_talking_potatoes.kis.service.PathService;

import java.util.ArrayList;

/**
 * package :  org.stupid_talking_potatoes.kis.path.controller
 * fileName : PathController
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@RestController
public class PathController {
    @Autowired
    PathService pathService;
    @GetMapping()
    public ArrayList<Path> getPathInfo(Double departureLongitude, Double departureLatitude, Double arrivalLongitude, Double arrivalLatitude){
        return null;
    }
}
