package org.stupid_talking_potatoes.kis.dto.tago;

import lombok.Data;

/**
 * package :  org.stupid_talking_potatoes.kis.tago.dto.request
 * fileName : TAGO_AroundNodeInfoRequest
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Data
public class TAGO_AroundNodeInfoRequest {
    private String serviceKey;
    private Double gpsLati;
    private Double pasLong;
}
