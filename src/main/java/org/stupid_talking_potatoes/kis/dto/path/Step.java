package org.stupid_talking_potatoes.kis.dto.path;

import lombok.*;
import org.json.JSONObject;
import org.stupid_talking_potatoes.kis.dto.node.NodeDto;
import org.stupid_talking_potatoes.kis.dto.route.ArrivalRoute;

import java.util.List;

/**
 * package :  org.stupid_talking_potatoes.kis.route.domain
 * fileName : Step
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Getter
@ToString
@NoArgsConstructor
public class Step {
    private String type;
    private Integer duration;
    private NodeDto departure;
    private NodeDto arrival;
    private List<ArrivalRoute> arrivalRouteList;
    
    public void setArrivalRouteList(List<ArrivalRoute> arrivalRouteList) {
        this.arrivalRouteList = arrivalRouteList;
    }
    
    public Step(String type, Integer duration) {
        this.type = type;
        this.duration = duration;
    }
    
    public void setDeparture(NodeDto node) {
        this.departure = node;
    }
    
    public void setArrival(NodeDto node) {
        this.arrival = node;
    }
    
    public static Step ofJSON(JSONObject step) {
        String type = (String) step.get("type");
        Integer duration = (Integer) step.get("duration");
        return Step.of(type,duration);
    }
    
    private static Step of(String type, Integer duration) {
        return new Step(type,duration);
    }
}
