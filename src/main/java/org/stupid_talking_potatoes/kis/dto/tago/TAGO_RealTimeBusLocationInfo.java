package org.stupid_talking_potatoes.kis.dto.tago;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class TAGO_RealTimeBusLocationInfo {
    @JsonProperty("nodeid")
    private String nodeId;
    @JsonProperty("nodenm")
    private String nodeNm;
    @JsonProperty("nodeord")
    private Integer nodeOrd;
    @JsonProperty("routenm")
    private Integer routeNm;
    @JsonProperty("routetp")
    private String routeTp;
    @JsonProperty("vehicleno")
    private String vehicleNo;
}
