package org.stupid_talking_potatoes.kis.dto.path;

import lombok.Data;

import java.util.ArrayList;

/**
 * package :  org.stupid_talking_potatoes.kis.path.dto
 * fileName : Path
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Data
public class Path {
    private Integer duration;
    private ArrayList<Step> stepList=new ArrayList<>();
}
