package org.stupid_talking_potatoes.kis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stupid_talking_potatoes.kis.dto.route.RealtimeBusLocationInfo;
import org.stupid_talking_potatoes.kis.service.NodeService;

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
    public RealtimeBusLocationInfo getRealtimeBusArrivalInfo(String nodeId){
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
