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
import org.stupid_talking_potatoes.kis.exception.BadRequestException;
import org.stupid_talking_potatoes.kis.service.NodeService;

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

    /**
     * 정류장 번호, 이름으로 검색하여 정류장 리스트를 응답하는 API
     * @param nodeNo 검색할 정류장 번호
     * @param nodeName 검색할 정류장 이름
     * @return 검색 결과 List
     */
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

    /**
     * 정류장 ID로 검색하여 해당 정류장에 대한 버스 도착 정보를 응답하는 API
     * @param nodeId 정류장 ID
     * @return 정류장 정보 및 해당 정류장의 버스 도착 정보 List
     */
    @GetMapping("/arrive-info")
    public ResponseEntity getRealtimeBusArrivalInfo(
            @RequestParam(name="id") String nodeId
    ) {
        RealtimeBusArrivalInfo response = nodeService.getRealtimeBusArrivalInfo(nodeId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * 좌표를 중심으로 반경 500m 내의 버스 정류장을 응답하는 API
     * @param longitude 경도
     * @param latitude 위도
     * @return 버스 정류장 List
     */
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
            throw BadRequestException.builder()
                    .message("longitude, latitude must be Double type")
                    .content(e.getMessage().replace("\"", "'")) // 에러 메시지에 \"가 있어 작은 따옴표로 변경
                    .build();
        }
        List<NodeDto> response = nodeService.getAroundNodeInfo(_longitude, _latitude);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}