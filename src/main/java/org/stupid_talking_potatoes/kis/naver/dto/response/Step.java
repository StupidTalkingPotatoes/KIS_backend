package org.stupid_talking_potatoes.kis.naver.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
public class Step{
    public String type;
    public String instruction;
    public Date departureTime;
    public Date arrivalTime;
    public int tripIdx;
    public int duration;
    public Route route;
    public Object departureCity;
    public ArrayList<String> subRouteNames;
    public ArrayList<Station> stations;
}
