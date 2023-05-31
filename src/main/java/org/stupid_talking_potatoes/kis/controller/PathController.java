package org.stupid_talking_potatoes.kis.controller;

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
public class PathController {
    @Autowired
    PathService pathService;
    @GetMapping("")
    public ResponseEntity<?> getPathInfo(@RequestParam Double departureLongitude,
                                         @RequestParam Double departureLatitude,
                                         @RequestParam Double arrivalLongitude,
                                         @RequestParam Double arrivalLatitude
    ){
        try{
            ArrayList<Path> paths = pathService.getPathInfo(departureLongitude, departureLatitude, arrivalLongitude, arrivalLatitude);
            return ResponseEntity.status(HttpStatus.OK).body(paths);
        } catch (ResponseStatusException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).header("error", String.valueOf(e)).body(new ArrayList<Path>());
        } catch (Exception e){
            log.error("internal error :",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", String.valueOf(e)).build();
        }
    }
}
