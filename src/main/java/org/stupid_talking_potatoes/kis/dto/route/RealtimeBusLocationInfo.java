package org.stupid_talking_potatoes.kis.dto.route;

import lombok.Data;
import org.stupid_talking_potatoes.kis.dto.node.PassingNode;
import org.stupid_talking_potatoes.kis.entity.Route;

import java.util.ArrayList;

/**
 * package :  org.stupid_talking_potatoes.kis.route.dto
 * fileName : RealtimeBusLocationResponse
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Data
public class RealtimeBusLocationInfo {
    private Route route;
    private ArrayList<PassingNode> passingNodeList;
}
