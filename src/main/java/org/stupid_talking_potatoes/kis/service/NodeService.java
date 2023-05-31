package org.stupid_talking_potatoes.kis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.stupid_talking_potatoes.kis.dto.node.NodeDto;
import org.stupid_talking_potatoes.kis.dto.node.RealtimeBusArrivalInfo;
import org.stupid_talking_potatoes.kis.dto.route.ArrivalRoute;
import org.stupid_talking_potatoes.kis.entity.Node;
import org.stupid_talking_potatoes.kis.exception.NotFoundException;
import org.stupid_talking_potatoes.kis.repository.NodeRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    
    public RealtimeBusArrivalInfo getRealtimeBusArrivalInfo(String nodeId){
        Node node = nodeRepository.findById(nodeId).orElseThrow(()-> new NotFoundException("Node is not found", nodeId));
        
        // get response from tago service
        List<ArrivalRoute> arrivalRoutes = tagoService.requestRealtimeBusArrivalInfo(nodeId);
        
        // set name of departure
        for (ArrivalRoute arrivalRoute: arrivalRoutes) {
            String departureName = routeService.getArrivalName(arrivalRoute.getRouteId());
            arrivalRoute.setDepartureName(departureName);
        }
        
        // sort by arrTime
        arrivalRoutes.sort(Comparator.comparing(ArrivalRoute::getArrTime));
        
        // set response
        RealtimeBusArrivalInfo response = new RealtimeBusArrivalInfo(new NodeDto(node), arrivalRoutes);
        return response;
    }
    
    public List<NodeDto> getAroundNodeInfo(Double longitude, Double latitude){
        // get response from tago service
        List<Node> aroundNodes = tagoService.requestAroundNodeInfo(longitude, latitude);
        
        // map to NodeDto
        List<NodeDto> aroundNodeDtos = new ArrayList<NodeDto>();
        for (Node node: aroundNodes)
            aroundNodeDtos.add(new NodeDto(node));
        
        return aroundNodeDtos;
    }
    
    public  NodeDto getNodeByNaverId(String naverNodeId){
        return null;
    }
}