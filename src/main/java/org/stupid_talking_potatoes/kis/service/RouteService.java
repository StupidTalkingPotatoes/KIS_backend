package org.stupid_talking_potatoes.kis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.stupid_talking_potatoes.kis.repository.RouteRepository;
import org.stupid_talking_potatoes.kis.dto.route.RealtimeBusLocationInfo;
import org.stupid_talking_potatoes.kis.dto.route.SearchedRoute;
import org.stupid_talking_potatoes.kis.entity.Route;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * package :  org.stupid_talking_potatoes.kis.route.service
 * fileName : RouteService
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Service
@RequiredArgsConstructor
public class RouteService {
    public static final String JSON = "json";
    public static final String GUMI_CITY_CODE = "37050";
    public static final String ONE_HUNDRED = "100";
    public static final String SERVICE_KEY = "serviceKey";
    public static final String TYPE = "_type";
    public static final String CITY_CODE = "cityCode";
    public static final String ROUTE_ID = "routeId";
    public static final String NUM_OF_ROWS = "numOfRows";
    private static final String SERVICE_KEY_VALUE = "xglcv%2F478Ggh0VzLUayLaKypd86Zoh0WYYjKLN5WOWUWvfaBjt%2FLjP46Nxx1lH2DmhmGNkN%2Fe8rBvQ62C5ljpg%3D%3D";

    private final RestTemplate restTemplate;
    private final RouteRepository routeRepository;

    public RealtimeBusLocationInfo getBusRouteInfo(String routeId) throws UnsupportedEncodingException {
        String uri = UriComponentsBuilder.fromHttpUrl("http://apis.data.go.kr/1613000/BusRouteInfoInqireService/getRouteAcctoThrghSttnList")
                .queryParam(URLEncoder.encode(SERVICE_KEY, StandardCharsets.UTF_8), SERVICE_KEY_VALUE)
                .queryParam(URLEncoder.encode(TYPE, StandardCharsets.UTF_8), URLEncoder.encode(JSON, StandardCharsets.UTF_8))
                .queryParam(URLEncoder.encode(CITY_CODE, StandardCharsets.UTF_8), URLEncoder.encode(GUMI_CITY_CODE, StandardCharsets.UTF_8))
                .queryParam(URLEncoder.encode(ROUTE_ID, StandardCharsets.UTF_8), URLEncoder.encode(routeId, StandardCharsets.UTF_8))
                .queryParam(URLEncoder.encode(NUM_OF_ROWS, StandardCharsets.UTF_8), URLEncoder.encode(ONE_HUNDRED, StandardCharsets.UTF_8))
                .toUriString();

        return new RealtimeBusLocationInfo();
    }

    public RealtimeBusLocationInfo getRealtimeBusLocationInfo(String routeId) throws UnsupportedEncodingException {
        String uri = UriComponentsBuilder.fromHttpUrl("http://apis.data.go.kr/1613000/BusLcInfoInqireService/getRouteAcctoBusLcList")
                .queryParam(URLEncoder.encode(SERVICE_KEY, StandardCharsets.UTF_8), SERVICE_KEY_VALUE)
                .queryParam(URLEncoder.encode(TYPE, StandardCharsets.UTF_8), URLEncoder.encode(JSON, StandardCharsets.UTF_8))
                .queryParam(URLEncoder.encode(CITY_CODE, StandardCharsets.UTF_8), URLEncoder.encode(GUMI_CITY_CODE, StandardCharsets.UTF_8))
                .queryParam(URLEncoder.encode(ROUTE_ID, StandardCharsets.UTF_8), URLEncoder.encode(routeId, StandardCharsets.UTF_8))
                .toUriString();

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
