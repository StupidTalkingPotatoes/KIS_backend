package org.stupid_talking_potatoes.kis.dto.tago;

import lombok.Data;

import java.util.List;

/**
 * package :  org.stupid_talking_potatoes.kis.tago.dto.response
 * fileName : TAGO_BusLocationInfoResponse
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Data
public class TAGO_BusLocationInfoResponse {
    private Integer nomOfRows;
    private Integer pageNo;
    private String totalCount;
    private List<TAGO_BusLocationInfo> items;
}
