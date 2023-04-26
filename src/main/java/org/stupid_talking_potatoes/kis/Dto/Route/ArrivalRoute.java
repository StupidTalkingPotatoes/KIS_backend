package org.stupid_talking_potatoes.kis.route.domain;

import lombok.Data;
import org.stupid_talking_potatoes.kis.route.domain.base.RouteBase;

/**
 * package :  org.stupid_talking_potatoes.kis.route.domain
 * fileName : ArrivalRoute
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Data
public class ArrivalRoute extends RouteBase {
    private Integer prevNodeCnt;
    private Integer arrTime;
    private String departureName;
}