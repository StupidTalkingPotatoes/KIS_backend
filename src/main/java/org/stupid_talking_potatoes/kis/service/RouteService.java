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
import org.stupid_talking_potatoes.kis.repository.RouteRepository;

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

    private final KneelingBusService kneelingBusService;

    private final TAGOService tagoService;

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

        Route route = routeOp.orElseThrow(() -> new NoSuchElementException(routeId + "에 해당하는 노선을 찾을 수 없습니다"));

        Set<RouteSeq> routeNodes = route.getRouteNodes();

        List<PassingNode> passingNodeList = routeNodes.stream()
                .map(PassingNode::fromRouteSeq)
                .sorted(comparingInt(PassingNode::getOrder))
                .toList();

        List<TAGO_RealTimeBusLocationInfo> realTimeBusLocationInfoList = tagoService.requestRealTimeBusLocationInfo(routeId);
    
        // 저상버스 필터링
        List<Integer> realtimeNodeOrderList = getFilteredNodeOrderList(realTimeBusLocationInfoList);
    
        return RealtimeBusLocationInfo.of(route, passingNodeList, realtimeNodeOrderList);
    }

    public String getArrivalName(String routeId){
        Route route = routeRepository.findByRouteId(routeId).orElseThrow(NoSuchElementException::new);
        return route.getEndNodeName();
    }

    /**
     * 현재 운행 중인 저상버스 필터링
     * @param realTimeBusLocationInfoList realTimeBusLocationInfoList(현재 운행 중인 버스 정보)
     * @return realtimeNodeOrderList(현재 운행 중인 저상버스 위치 리스트)
     */
    private List<Integer> getFilteredNodeOrderList(List<TAGO_RealTimeBusLocationInfo> realTimeBusLocationInfoList) {
        return realTimeBusLocationInfoList.stream()
                .filter(realTimeBusLocationInfo -> kneelingBusService.isKneelingBus(realTimeBusLocationInfo.getVehicleNo()))
                .map(TAGO_RealTimeBusLocationInfo::getNodeOrd)
                .toList();
    }

    public String getDeparture(String routeId){
        return null;
    }

}
