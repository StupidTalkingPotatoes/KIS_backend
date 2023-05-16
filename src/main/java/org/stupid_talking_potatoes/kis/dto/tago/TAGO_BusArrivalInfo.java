package org.stupid_talking_potatoes.kis.dto.tago;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * package :  org.stupid_talking_potatoes.kis.tago.dto
 * fileName : TAGO_BusArrivalInfo
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseStrategy.class)
public class TAGO_BusArrivalInfo {
    private String nodeId;
    private String nodeNm;
    private String routeId;
    private String routeNo;
    private String routeTp;
    private Integer arrPrevStationCnt;
    private String vehicleTp;
    private Integer arrTime;
}
