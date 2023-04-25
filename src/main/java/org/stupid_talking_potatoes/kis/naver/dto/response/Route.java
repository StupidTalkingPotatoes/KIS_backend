package org.stupid_talking_potatoes.kis.naver.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Route{
    public int id;
    public String name;
    public String longName;
    public Object aliasName;
    public Type type;
    public boolean realtime;
    public boolean useInterval;
    public Operation operation;
    public Object color;
    public Platform platform;
    public String headsign;
    public ArrayList<BusInterval> busIntervals;
    public Arrival arrival;
    public Object extra;
    public Object exType;
    public boolean canReserve;
}
