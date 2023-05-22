package org.stupid_talking_potatoes.kis.dto.tago;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * package :  org.stupid_talking_potatoes.kis.tago.dto
 * fileName : TAGO_BusLocationInfo
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Data
public class TAGO_BusLocationInfo {
    @JsonProperty("routeid")
    private String routeId;
    @JsonProperty("gpslati")
    private Double gpsLati;
    @JsonProperty("gpslong")
    private Double gpsLong;
    @JsonProperty("nodeord")
    private Integer nodeOrd;
    @JsonProperty("nodenm")
    private String nodeNm;
    @JsonProperty("nodeid")
    private String nodeId;
    @JsonProperty("nodeno")
    private String nodeNo;
}
