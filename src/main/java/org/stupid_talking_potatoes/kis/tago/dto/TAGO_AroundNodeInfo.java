package org.stupid_talking_potatoes.kis.tago.dto;

import lombok.Data;

/**
 * package :  org.stupid_talking_potatoes.kis.tago.dto
 * fileName : TAGO_AroundNodeInfo
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Data
public class TAGO_AroundNodeInfo {
    private Double gpsLati;
    private Double gpsLong;
    private String nodeId;
    private String nodeNm;
    private Integer cityCode;
}
