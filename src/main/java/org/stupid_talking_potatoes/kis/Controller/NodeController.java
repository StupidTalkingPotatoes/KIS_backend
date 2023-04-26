package org.stupid_talking_potatoes.kis.node.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stupid_talking_potatoes.kis.node.service.NodeService;
import org.stupid_talking_potatoes.kis.route.dto.RealtimeBusLocationResponse;

import java.util.ArrayList;

/**
 * package :  org.stupid_talking_potatoes.kis.node.controller
 * fileName : NodeController
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@RestController
public class NodeController {
    @Autowired
    NodeService nodeService;
    @GetMapping()
    public RealtimeBusLocationResponse getRealtimeBusArrivalInfo(String nodeId){
        return null;
    }
    @GetMapping()
    public ArrayList<?> getAroundNodeInfo(Double longitude,Double latitude){
        
        return null;
    }
    @GetMapping()
    public ArrayList<?> getNodeList(String nodeId, String nodeName){
        return null;
    }
}
