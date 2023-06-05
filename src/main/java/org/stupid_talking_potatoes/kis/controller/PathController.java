package org.stupid_talking_potatoes.kis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.stupid_talking_potatoes.kis.dto.path.Path;
import org.stupid_talking_potatoes.kis.service.PathService;

import java.io.IOException;
import java.util.ArrayList;

/**
 * package :  org.stupid_talking_potatoes.kis.path.controller
 * fileName : PathController
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Slf4j
@RestController
@RequestMapping("/paths")
@RequiredArgsConstructor
public class PathController {
    private final PathService pathService;
    
    @GetMapping("")
    public ResponseEntity<?> getPathInfo(@RequestParam Double departureLongitude,
                                         @RequestParam Double departureLatitude,
                                         @RequestParam Double arrivalLongitude,
                                         @RequestParam Double arrivalLatitude
    ) throws IOException {
        ArrayList<Path> paths = pathService.getPathInfo(departureLongitude, departureLatitude, arrivalLongitude, arrivalLatitude);
        return ResponseEntity.status(HttpStatus.OK).body(paths);
    }
}
