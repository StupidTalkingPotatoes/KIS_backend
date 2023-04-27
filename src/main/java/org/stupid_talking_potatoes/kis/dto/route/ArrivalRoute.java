package org.stupid_talking_potatoes.kis.dto.route;

import lombok.Getter;
import lombok.Setter;
import org.stupid_talking_potatoes.kis.entity.RouteBase;

/**
 * package :  org.stupid_talking_potatoes.kis.route.domain
 * fileName : ArrivalRoute
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Getter
@Setter
public class ArrivalRoute extends RouteBase {
    private Integer prevNodeCnt;
    private Integer arrTime;
    private String departureName;
}
