package org.stupid_talking_potatoes.kis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysql.cj.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.stupid_talking_potatoes.kis.dto.node.PassingNode;
import org.stupid_talking_potatoes.kis.dto.tago.TAGO_RealTimeBusLocationInfo;
import org.stupid_talking_potatoes.kis.entity.Route;
import org.stupid_talking_potatoes.kis.repository.RouteRepository;
import org.stupid_talking_potatoes.kis.dto.route.RealtimeBusLocationInfo;
import org.stupid_talking_potatoes.kis.dto.route.SearchedRoute;

import java.io.UnsupportedEncodingException;
import java.util.*;
import org.stupid_talking_potatoes.kis.entity.Route;
import org.stupid_talking_potatoes.kis.repository.RouteRepository;

/**
 * package :  org.stupid_talking_potatoes.kis.route.service
 * fileName : RouteService
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;

    private final KneelingBusService kneelingBusService;

    private final TAGOService tagoService;

    /**
     * searchApi: keyword를 가지고 Route 검색
     * @param keyword 검색하고자 하는 keyword(RouteNo)
     * @return SearchedRouteList
     */
    public List<SearchedRoute> getRouteList(String keyword){
        if (StringUtils.isNullOrEmpty(keyword)) {
            return Collections.emptyList();
        }

        List<Route> routes = routeRepository.findAllByRouteNoContaining(keyword);

        List<SearchedRoute> searchedRoutes = new ArrayList<>();
        for (Route route : routes) {
            SearchedRoute searchedRoute = SearchedRoute.fromEntity(route);
            searchedRoutes.add(searchedRoute);
        }

        return searchedRoutes;
    }

    /**
     * routeId에 해당하는 경로 조회 및 경로 상에 있는 실시간 저상버스 위치 정보
     * @param routeId routeId
     * @return RealtimeBusLocationInfo
     */
    public RealtimeBusLocationInfo getBusRouteInfo(String routeId) {

        List<PassingNode> passingNodeList = tagoService.getPassingNodeList(routeId);

        Optional<Route> routeOp = routeRepository.findByRouteId(routeId);

        Route route = routeOp.orElseThrow(()-> new NoSuchElementException(routeId + "에 해당하는 노선을 찾을 수 없습니다"));

        List<TAGO_RealTimeBusLocationInfo> realTimeBusLocationInfoList = tagoService.requestRealTimeBusLocationInfo(routeId);

        // 저상버스 필터링
        List<Integer> realtimeNodeOrderList = getFilteredNodeOrderList(realTimeBusLocationInfoList);

        return RealtimeBusLocationInfo.of(route, passingNodeList,realtimeNodeOrderList );
    
    public String getArrivalName(String routeId){
        Route route = routeRepository.findByRouteId(routeId).orElseThrow(()-> new NoSuchElementException());
        return route.getEndNodeName();
    }

    /**
     * 현재 운행 중인 저상버스 필터링
     * @param realTimeBusLocationInfoList realTimeBusLocationInfoList(현재 운행 중인 버스 정보)
     * @return realtimeNodeOrderList(현재 운행 중인 저상버스 위치 리스트)
     */
    private List<Integer> getFilteredNodeOrderList(List<TAGO_RealTimeBusLocationInfo> realTimeBusLocationInfoList) {
        List<Integer> realtimeNodeOrderList = new ArrayList<>();

        for (TAGO_RealTimeBusLocationInfo realTimeBusLocationInfo : realTimeBusLocationInfoList) {
            String vehicleNo = realTimeBusLocationInfo.getVehicleNo();
            if (kneelingBusService.isKneelingBus(vehicleNo)) {
                realtimeNodeOrderList.add(realTimeBusLocationInfo.getNodeOrd());
            }
        }
        return realtimeNodeOrderList;
    }

    public String getDeparture(String routeId){
        return null;
    }

}
