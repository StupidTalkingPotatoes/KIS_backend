package org.stupid_talking_potatoes.kis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.stupid_talking_potatoes.kis.dto.node.NodeDto;
import org.stupid_talking_potatoes.kis.dto.node.RealtimeBusArrivalInfo;
import org.stupid_talking_potatoes.kis.dto.route.ArrivalRoute;
import org.stupid_talking_potatoes.kis.entity.Node;
import org.stupid_talking_potatoes.kis.exception.NotFoundException;
import org.stupid_talking_potatoes.kis.repository.NodeRepository;
import org.stupid_talking_potatoes.kis.service.tago.AroundNodeService;
import org.stupid_talking_potatoes.kis.service.tago.BusArrivalInfoService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * package :  org.stupid_talking_potatoes.kis.node.service
 * fileName : NodeService
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Service
@RequiredArgsConstructor
public class NodeService {
    
    private final NodeRepository nodeRepository;
    private final TAGOService tagoService;
    private final RouteService routeService;
    private final AroundNodeService aroundNodeService;
    private final BusArrivalInfoService busArrivalInfoService;
    
    
    /**
     * nodeNo와 nodeName을 기준으로 node 테이블의 요소를 NodeDto에 담아 리스트형태로 반환
     * @param nodeNo 정류장 번호
     * @param nodeName 정류장명
     * @return 정류장 정보가 담긴 NodeDto 리스트
     */
    public List<NodeDto> getNodeList(String nodeNo, String nodeName){
        List<NodeDto> list = Collections.emptyList();

        if (nodeNo != null) {
            Optional<Node> node = nodeRepository.findByNodeNo(nodeNo);
            if (node.isPresent()) { // if node exists
                list.add(new NodeDto(node.get()));
            } // else, not error but empty list
        }
        else if (nodeName != null) {
            List<Node> nodes = nodeRepository.findByNodeNameContaining(nodeName);
            return nodes.stream().map(NodeDto::new).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * nodeId를 기준으로 외부 API로부터 해당 정류장의 실시간 버스 도착 정보를 받아와
     * 해당 버스의 도착지를 추가하여 리턴
     * @param nodeId 정류장 ID
     * @return RealtimeBusArrivalInfo (노드 정보 및 버스 도착 정보 리스트)
     */
    public RealtimeBusArrivalInfo getRealtimeBusArrivalInfo(String nodeId){
        Node node = nodeRepository.findById(nodeId).orElseThrow(
                ()-> new NotFoundException("Node is not found", nodeId)
        );
        
        // get response from tago service
        List<ArrivalRoute> arrivalRoutes = busArrivalInfoService.requestRealtimeBusArrivalInfo(nodeId);
        
        // set name of departure
        for (ArrivalRoute arrivalRoute: arrivalRoutes) {
            String departureName = routeService.getArrivalName(arrivalRoute.getRouteId());
            arrivalRoute.setDepartureName(departureName);
        }
        
        // sort by arrTime
        arrivalRoutes.sort(Comparator.comparing(ArrivalRoute::getArrTime));
        
        // set and return response
        return new RealtimeBusArrivalInfo(new NodeDto(node), arrivalRoutes);
    }

    /**
     * 좌표 값을 바탕으로 외부 API로부터 주변 정류장 리스트를 받아와 dto로 매핑 후 반환하는 함수
     * @param longitude 경도
     * @param latitude 위도
     * @return 주변 정류장 리스트
     */
    public List<NodeDto> getAroundNodeInfo(Double longitude, Double latitude){
        // get response from tago service
        List<Node> aroundNodes = aroundNodeService.requestAroundNodeInfo(longitude, latitude);
        
        // map to NodeDto and return
        return aroundNodes.stream().map(NodeDto::new).toList();
    }
}