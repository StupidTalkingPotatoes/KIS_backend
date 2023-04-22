package org.stupid_talking_potatoes.kis.tago.dto;

import lombok.Data;

/**
 * package :  org.stupid_talking_potatoes.kis.tago.dto
 * fileName : TAGO_BusArrivalInfo
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Data
public class TAGO_BusArrivalInfo {
    private String nodeId;
    private String nodeNm;
    private String routeId;
    private String routeNo;
    private String routeTp;
    private String arrPrevStationCnt;
    private Integer vehicleTp;
    private Integer arrTime;
}
