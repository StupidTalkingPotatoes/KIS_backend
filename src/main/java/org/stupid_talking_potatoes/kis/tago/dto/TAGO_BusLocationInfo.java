package org.stupid_talking_potatoes.kis.tago.dto;

import lombok.Data;

/**
 * package :  org.stupid_talking_potatoes.kis.tago.dto
 * fileName : TAGO_BusLocationInfo
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Data
public class TAGO_BusLocationInfo {
    private String routeNm;
    private Double gpsLati;
    private Double gpsLong;
    private Integer nodeOrd; //이거 Integer로하는게 맞는가??? 순행 역행 따져서 Integer한거같긴한데 어떤게 순행인지 정하는것도 일일거같아서 문자열이 더 편할거같아서
    private String nodeNm;
    private String nodeId;
    private String routeTp;
    private String vehicleNo;
}
