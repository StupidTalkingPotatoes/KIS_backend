package org.stupid_talking_potatoes.kis.dto.path;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.json.JSONObject;

import java.util.List;

/**
 * package :  org.stupid_talking_potatoes.kis.path.dto
 * fileName : Path
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Getter
@ToString
@NoArgsConstructor
public class Path {
    //TODO: 라벨 추가
    private Integer duration;
    private List<Step> stepList;
    
    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
    }
    
    public Path(Integer duration) {
        this.duration=duration;
    }
    
    public static Path ofJSON(JSONObject path) {
        Integer duration = (Integer) path.get("duration");
        return Path.of(duration);
    }
    
    private static Path of(Integer duration) {
        return new Path(duration);
    }
}
