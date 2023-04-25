package org.stupid_talking_potatoes.kis.naver.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Station{
    public int id;
    public String placeId;
    public String name;
    public Object longName;
    public String displayName;
    public String displayCode;
    public boolean stop;
    public boolean realtime;
    public Auxi aux;
    public BusStopAux busStopAux;
    public int duration;
    public int distance;
    public int pointIndex;
}
