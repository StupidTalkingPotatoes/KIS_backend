package org.stupid_talking_potatoes.kis.dto.tago;

import lombok.Data;

/**
 * package :  org.stupid_talking_potatoes.kis.tago.dto.request
 * fileName : TAGO_BusArrivalInfoRequest
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Data
public class TAGO_BusArrivalInfoRequest {
    private String serviceKey;//@Valude 사용 고려
    private Integer cityCode;
    private String nodeId;
    private String routeId;
}
