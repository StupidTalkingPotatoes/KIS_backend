package org.stupid_talking_potatoes.kis.dto.tago;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * package :  org.stupid_talking_potatoes.kis.tago.dto.response
 * fileName : TAGO_BusArrivalInfoResponse
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Data
@Getter
@Setter
public class TAGO_BusArrivalInfoResponse {
    private Integer nomOfRows;
    private Integer pageNo;
    private String totalCount;
    private ArrayList<TAGO_BusArrivalInfo> items;
}
