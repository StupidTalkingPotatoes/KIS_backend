package org.stupid_talking_potatoes.kis.naver.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter

public class Res{
    public String status;
    public Context context;
    public ArrayList<Path> paths;
}
