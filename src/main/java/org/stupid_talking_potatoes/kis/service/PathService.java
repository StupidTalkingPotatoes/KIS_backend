package org.stupid_talking_potatoes.kis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.stupid_talking_potatoes.kis.dto.path.Path;

import java.io.IOException;
import java.util.ArrayList;

/**
 * package :  org.stupid_talking_potatoes.kis.path.service
 * fileName : PathService
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Service
@RequiredArgsConstructor
public class PathService {
    private final NaverService naverService;
    public ArrayList<Path> getPathInfo(Double departureLongitude, Double departureLatitude, Double arrivalLongitude, Double arrivalLatitude) throws ResponseStatusException, IOException {
        return naverService.getTransportationInfo(departureLongitude,departureLatitude,arrivalLongitude,arrivalLatitude);
    }
}
