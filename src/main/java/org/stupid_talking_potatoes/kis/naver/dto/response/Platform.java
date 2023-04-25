package org.stupid_talking_potatoes.kis.naver.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter
public class Platform{
    public String status;
    public Type type;
    public ArrayList<String> doors;
    public int id;
    public String name;
}
