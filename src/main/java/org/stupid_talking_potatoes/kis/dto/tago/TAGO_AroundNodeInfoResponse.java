package org.stupid_talking_potatoes.kis.dto.tago;

import lombok.Data;

import java.util.List;

/**
 * package :  org.stupid_talking_potatoes.kis.tago.dto.response
 * fileName : TAGO_AroundNodeInfoResponse
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Data
public class TAGO_AroundNodeInfoResponse {
    private Integer nomOfRows;
    private Integer pageNo;
    private String totalCount;//어디서는 cnt고 여기는 Count라 맞추는게 나을듯??
    private List<TAGO_AroundNodeInfo> items;
}
