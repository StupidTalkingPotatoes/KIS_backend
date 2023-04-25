package org.stupid_talking_potatoes.kis.naver.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item{
    public String status;
    public String plateNo;
    public boolean lowFloor;
    public int remainingTime;
    public int remainingStop;
    public Object remainingSeat;
    public Congestion congestion;
    public boolean last;
}
