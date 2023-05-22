package org.stupid_talking_potatoes.kis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.stupid_talking_potatoes.kis.dto.node.NodeDto;
import org.stupid_talking_potatoes.kis.dto.node.RealtimeBusArrivalInfo;
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
    public ResponseEntity getNodeList(
            @RequestParam(name="no", required = false) String nodeNo,
            @RequestParam(name="name", required = false) String nodeName
    ) {
        List<NodeDto> response = nodeService.getNodeList(nodeNo, nodeName);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/arrive-info")
    public ResponseEntity getRealtimeBusArrivalInfo(
            @RequestParam(name="id") String nodeId
    ) {
        RealtimeBusArrivalInfo response = nodeService.getRealtimeBusArrivalInfo(nodeId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("")
    public ResponseEntity getAroundNodeInfo(
            @RequestParam String longitude,
            @RequestParam String latitude
    ) {
        // 데이터 타입을 Double로 해놓으면 다른 요청이 들어왔을 때 에러 반환하기 때문에 String으로 해놓고 처리
        Double _longitude = 0.0, _latitude = 0.0;
        try {
            _longitude = Double.valueOf(longitude);
            _latitude = Double.valueOf(latitude);
        } catch (NumberFormatException e) {
            // TODO: Handle exception
            throw new RuntimeException();
        }
        List<NodeDto> response = nodeService.getAroundNodeInfo(_longitude, _latitude);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
