package org.stupid_talking_potatoes.kis.dto.path;

import lombok.*;
import org.json.JSONObject;
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
    private String departure;//이름
    private String arrival;//이름
    private List<ArrivalRoute> arrivalRouteList;

    
    public Step(String type, Integer duration) {
        this.type = type;
        this.duration = duration;
    }
    
    public void setDeparture(String departure) {
        this.departure = departure;
    }
    
    public void setArrival(String arrival) {
        this.arrival = arrival;
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
