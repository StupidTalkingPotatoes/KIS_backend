package org.stupid_talking_potatoes.kis.naver.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
public class Path{
    public int idx;
    public String mode;
    public String type;
    public String optimizationMethod;
    public ArrayList<String> labels;
    public Date departureTime;
    public Date arrivalTime;
    public int duration;
    public int intercityDuration;
    public int distance;
    public int walkingDuration;
    public ArrayList<PathLabel> pathLabels;
    public int fare;
    public ArrayList<Fare> fares;
    public ArrayList<Leg> legs;
}
