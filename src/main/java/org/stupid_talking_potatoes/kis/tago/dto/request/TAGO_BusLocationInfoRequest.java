package org.stupid_talking_potatoes.kis.tago.dto.request;

import lombok.Data;

/**
 * package :  org.stupid_talking_potatoes.kis.tago.dto.request
 * fileName : TAGO_BusLocationInfoRequest
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Data
public class TAGO_BusLocationInfoRequest {
    private String serviceKey;
    private Integer cityCode;
    private String routeId;
}
