package org.stupid_talking_potatoes.kis.dto.tago;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * package :  org.stupid_talking_potatoes.kis.tago.dto
 * fileName : TAGO_AroundNodeInfo
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Data
@AllArgsConstructor // test 용도
@NoArgsConstructor  // json parse 용도
public class TAGO_AroundNodeInfo {
    private Integer cityCode;
    private Double gpsLati;
    private Double gpsLong;
    private String nodeId;
    private String nodeNm;
    private Integer nodeNo;
}
