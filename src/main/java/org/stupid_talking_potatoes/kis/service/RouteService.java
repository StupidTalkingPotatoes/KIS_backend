package org.stupid_talking_potatoes.kis.service;

import com.mysql.cj.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.stupid_talking_potatoes.kis.dto.node.PassingNode;
import org.stupid_talking_potatoes.kis.dto.route.RealtimeBusLocationInfo;
import org.stupid_talking_potatoes.kis.dto.route.SearchedRoute;
import org.stupid_talking_potatoes.kis.dto.tago.TAGO_RealTimeBusLocationInfo;
import org.stupid_talking_potatoes.kis.entity.Route;
import org.stupid_talking_potatoes.kis.entity.RouteSeq;
import org.stupid_talking_potatoes.kis.exception.NotFoundException;
import org.stupid_talking_potatoes.kis.repository.RouteRepository;
import org.stupid_talking_potatoes.kis.service.tago.RealTimeBusLocationInfoService;

import java.util.*;

import static java.util.Comparator.comparingInt;

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
    private final RealTimeBusLocationInfoService realTimeBusLocationInfoService;

    /**
     * searchApi: routeNo를 가지고 Route 검색
     * @param routeNo 검색하고자 하는 routeNo(RouteNo)
     * @return SearchedRouteList
     */
    public List<SearchedRoute> getRouteList(String routeNo){
        if (StringUtils.isNullOrEmpty(routeNo)) {
            return Collections.emptyList();
        }

        List<Route> routes = routeRepository.findAllByRouteNoContaining(routeNo);

        return routes.stream()
                .map(SearchedRoute::fromEntity)
                .toList();
    }

    /**
     * routeId에 해당하는 경로 조회 및 경로 상에 있는 실시간 저상버스 위치 정보
     * @param routeId routeId
     * @return RealtimeBusLocationInfo
     */
    public RealtimeBusLocationInfo getRealtimeBusLocationInfo(String routeId) {
        Optional<Route> routeOp = routeRepository.findByRouteId(routeId);

        Route route = routeOp.orElseThrow(() -> new NotFoundException("Route is not found", routeId));

        Set<RouteSeq> routeNodes = route.getRouteNodes();

        List<PassingNode> passingNodeList = routeNodes.stream()
                .map(PassingNode::fromRouteSeq)
                .sorted(comparingInt(PassingNode::getOrder))
                .toList();
        
        // 실시간 저상버스 위치 리스트
        List<Integer> realtimeNodeOrderList = realTimeBusLocationInfoService.getRealtimeNodeOrderList(routeId);
        return RealtimeBusLocationInfo.of(route, passingNodeList, realtimeNodeOrderList);
    }

    public String getArrivalName(String routeId){
        Route route = routeRepository.findByRouteId(routeId).orElseThrow(NoSuchElementException::new);
        return route.getEndNodeName();
    }


    public String getDeparture(String routeId){
        return null;
    }

}
