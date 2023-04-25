package org.stupid_talking_potatoes.kis.naver.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusInterval{
    public String name;
    public int min;
    public int max;
    public Object count;
}
