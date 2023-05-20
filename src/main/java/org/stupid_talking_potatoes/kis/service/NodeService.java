package org.stupid_talking_potatoes.kis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.stupid_talking_potatoes.kis.dto.node.NodeDto;
import org.stupid_talking_potatoes.kis.dto.node.RealtimeBusArrivalInfo;
import org.stupid_talking_potatoes.kis.dto.route.ArrivalRoute;
import org.stupid_talking_potatoes.kis.entity.Node;
import org.stupid_talking_potatoes.kis.repository.NodeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
     * nodeNo와 nodeName을 기준으로 node테이블의 요소를 NodeDto에 담아 리스트형태로 반환
     *
     * @param nodeNo nodeNo
     * @param nodeName nodeName
     * @return {@link ArrayList}
     * @see ArrayList
     * @see NodeDto
     */
    public ArrayList<NodeDto> getNodeList(String nodeNo, String nodeName){
        ArrayList<NodeDto> nodeList = new ArrayList<>();

        if (nodeNo != null) {
            Optional<Node> node = nodeRepository.findByNodeNo(nodeNo);
            if (node.isPresent()) { // if node exists
                nodeList.add(new NodeDto(node.get()));
            } // else, not error but empty list
        }
        else if (nodeName != null) {
            List<Node> nodes = nodeRepository.findByNodeNameContaining(nodeName);
            for (Node node : nodes) {
                nodeList.add(new NodeDto(node));
            }
        }

        return nodeList;
    }
    
    public RealtimeBusArrivalInfo getRealtimeBusArrivalInfo(String nodeId){
        Node node = nodeRepository.findById(nodeId).orElseThrow(()-> new NoSuchElementException());

        // get response from tago service
        ArrayList<ArrivalRoute> arrivalRoutes = tagoService.requestRealtimeBusArrivalInfo(nodeId);

        // set name of departure
        for (ArrivalRoute arrivalRoute: arrivalRoutes) {
            String departureName = routeService.getArrivalName(arrivalRoute.getRouteId());
            arrivalRoute.setDepartureName(departureName);
        }

        // set response
        RealtimeBusArrivalInfo response = new RealtimeBusArrivalInfo();
        response.setNodeDto(new NodeDto(node));
        response.setArrivalRouteList(arrivalRoutes);
        return response;
    }

    public ArrayList<NodeDto> getAroundNodeInfo(Double longitude, Double latitude){
        return null;
    }

    public  NodeDto getNodeByNaverId(String naverNodeId){
        return null;
    }
}
