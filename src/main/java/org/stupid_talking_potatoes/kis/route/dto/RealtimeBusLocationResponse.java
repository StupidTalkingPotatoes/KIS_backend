package org.stupid_talking_potatoes.kis.route.dto;

import lombok.Data;
import org.stupid_talking_potatoes.kis.node.domain.dto.PassingNode;
import org.stupid_talking_potatoes.kis.route.domain.Route;

import java.util.ArrayList;

/**
 * package :  org.stupid_talking_potatoes.kis.route.dto
 * fileName : RealtimeBusLocationResponse
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Data
public class RealtimeBusLocationResponse {
    private Route route;
    private ArrayList<PassingNode> passingNodeList;
}
