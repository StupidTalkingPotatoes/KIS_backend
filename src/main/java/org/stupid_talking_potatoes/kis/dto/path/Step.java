package org.stupid_talking_potatoes.kis.dto.path;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.stupid_talking_potatoes.kis.dto.route.ArrivalRoute;

import java.util.ArrayList;

/**
 * package :  org.stupid_talking_potatoes.kis.route.domain
 * fileName : Step
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Getter
@Setter
@ToString
public class Step {
    private String type;
    private Integer duration;
    private String departure;
    private String arrival;
    private ArrayList<ArrivalRoute> arrivalRouteList=new ArrayList<>();
}
