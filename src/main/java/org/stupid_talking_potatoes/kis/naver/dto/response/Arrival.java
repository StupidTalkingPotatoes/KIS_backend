package org.stupid_talking_potatoes.kis.naver.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter
public class Arrival{
    public String status;
    public int stationId;
    public int routeId;
    public ArrayList<Item> items;
}
