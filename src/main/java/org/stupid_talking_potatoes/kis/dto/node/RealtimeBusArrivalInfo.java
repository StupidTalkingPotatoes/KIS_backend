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
    private NodeDto nodeDto;
    private ArrayList<ArrivalRoute> arrivalRouteList;
}
