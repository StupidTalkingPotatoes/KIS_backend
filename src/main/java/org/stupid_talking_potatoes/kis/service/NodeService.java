package org.stupid_talking_potatoes.kis.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.stupid_talking_potatoes.kis.dto.node.NodeDto;
import org.stupid_talking_potatoes.kis.dto.node.RealtimeBusArrivalInfo;
import org.stupid_talking_potatoes.kis.entity.Node;
import org.stupid_talking_potatoes.kis.repository.NodeRepository;

import java.util.ArrayList;
import java.util.List;
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

    public ArrayList<NodeDto> getNodeList(String nodeNo, String nodeName){
        ArrayList<NodeDto> nodeList = new ArrayList<>();
        if (nodeNo != null) {
            Optional<Node> node = nodeRepository.findByNodeNo(nodeNo);
            if (node.isPresent()) { // if node exists
                nodeList.add(new NodeDto(node.get()));
            }
        } else if (nodeName != null) {
            List<Node> nodes = nodeRepository.findByNodeNameContaining(nodeName);
            for (Node node : nodes) {
                nodeList.add(new NodeDto(node));
            }
        }
        return nodeList;
    }
    
    public RealtimeBusArrivalInfo getRealtimeBusArrivalInfo(String nodeId){

        return null;
    }

    public ArrayList<NodeDto> getAroundNodeInfo(Double longitude, Double latitude){
        return null;
    }

    public  NodeDto getNodeByNaverId(String naverNodeId){
        return null;
    }
}
