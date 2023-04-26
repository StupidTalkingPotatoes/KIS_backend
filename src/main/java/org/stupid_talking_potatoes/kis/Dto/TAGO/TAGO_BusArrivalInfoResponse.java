package org.stupid_talking_potatoes.kis.tago.dto.response;

import lombok.Data;
import org.stupid_talking_potatoes.kis.tago.dto.TAGO_BusArrivalInfo;

import java.util.ArrayList;

/**
 * package :  org.stupid_talking_potatoes.kis.tago.dto.response
 * fileName : TAGO_BusArrivalInfoResponse
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Data
public class TAGO_BusArrivalInfoResponse {
    private Integer nomOfRows;
    private Integer pageNo;
    private String totalCount;
    private ArrayList<TAGO_BusArrivalInfo> items;
}
