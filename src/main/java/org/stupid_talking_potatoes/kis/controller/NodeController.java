package org.stupid_talking_potatoes.kis.controller;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.stupid_talking_potatoes.kis.dto.node.NodeDto;
import org.stupid_talking_potatoes.kis.dto.route.RealtimeBusLocationInfo;
import org.stupid_talking_potatoes.kis.entity.Node;
import org.stupid_talking_potatoes.kis.service.NodeService;

import java.util.ArrayList;
import java.util.List;

/**
 * package :  org.stupid_talking_potatoes.kis.node.controller
 * fileName : NodeController
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/nodes")
public class NodeController {
    private final NodeService nodeService;

    @GetMapping("/search")
    public ResponseEntity getNodeList(@RequestParam String nodeNo, @RequestParam String nodeName) {
        List<NodeDto> response = nodeService.getNodeList(nodeNo, nodeName);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/arrive-info")
    public ResponseEntity getRealtimeBusArrivalInfo(@RequestParam String nodeId) {
        RealtimeBusLocationInfo response = nodeService.getRealtimeBusArrivalInfo(nodeId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/")
    public ArrayList<?> getAroundNodeInfo(Double longitude, Double latitude) {

        return null;
    }
}
