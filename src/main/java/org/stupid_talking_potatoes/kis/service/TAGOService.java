package org.stupid_talking_potatoes.kis.service;

import org.springframework.stereotype.Service;
import org.stupid_talking_potatoes.kis.dto.node.PassingNode;
import org.stupid_talking_potatoes.kis.dto.route.ArrivalRoute;
import org.stupid_talking_potatoes.kis.entity.Node;

import java.util.ArrayList;

/**
 * package :  org.stupid_talking_potatoes.kis.tago.service
 * fileName : TAGOService
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Service
public class TAGOService {
    private String serviceKey;
    public ArrayList<ArrivalRoute> requestRealtimeBusArrivalInfo(String nodeId){
        return null;
    }
    public ArrayList<ArrivalRoute> requestRealtimeSpecificBusLocationInfo(String nodeId, String routeId){
        return null;
    }
    public ArrayList<PassingNode> requestRealtimeBusLocationInfo(String nodeId){
        return null;
    }
    public ArrayList<Node> requestAroundNodeInfo(Double longitude, Double latitude){
        return null;
    }
//    public ArrayList<ArrivalRoute> filterKneelingBus(List<TAGO_BusArrivalInfo> busArrivalInfoList){
//        return null;
//    }
//    public ArrayList<ArrivalRoute> filterKneelingBus(List<TAGO_BusLocationInfo> busLocationInfoList){
//        return null;
//    }
//    메서드명이 동일해서 오류를 일으킴
//    오버로딩을 하는게 맞는가에 고민이 좀 필요할듯?
}
