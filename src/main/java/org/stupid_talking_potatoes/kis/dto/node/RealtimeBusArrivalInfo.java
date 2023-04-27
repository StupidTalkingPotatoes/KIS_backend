package org.stupid_talking_potatoes.kis.dto.node;

import lombok.Data;
import org.stupid_talking_potatoes.kis.dto.route.ArrivalRoute;

import java.util.ArrayList;

/**
 * package :  org.stupid_talking_potatoes.kis.node.dto
 * fileName : RealtimeBusArrivalInfo
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Data
public class RealtimeBusArrivalInfo {
    private NodeDto nodeDto;//데이터 전송을 위한 요소로 판단되어 DTO로 변경
    private ArrayList<ArrivalRoute> arrivalRouteList;
}
