package org.stupid_talking_potatoes.kis.naver.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter
public class Fare{
    public ArrayList<ArrayList<?>> routes;//흠,,,
    public int fare;
    public int tripIdx;
}
