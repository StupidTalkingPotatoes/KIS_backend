package org.stupid_talking_potatoes.kis.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stupid_talking_potatoes.kis.dto.node.NodeDto;
import org.stupid_talking_potatoes.kis.repository.NodeRepository;
import org.stupid_talking_potatoes.kis.dto.route.RealtimeBusLocationInfo;

import java.util.ArrayList;

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

    public ArrayList<NodeDto> getNodeList(String nodeNo, String nodeName){
        return null;
    }
    
    public RealtimeBusLocationInfo getRealtimeBusArrivalInfo(String nodeId){
        return null;
    }

    public ArrayList<NodeDto> getAroundNodeInfo(Double longitude, Double latitude){
        return null;
    }

    public  NodeDto getNodeByNaverId(String naverNodeId){
        return null;
    }
}
