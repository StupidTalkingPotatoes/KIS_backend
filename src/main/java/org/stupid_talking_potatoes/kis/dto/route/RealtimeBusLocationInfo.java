package org.stupid_talking_potatoes.kis.dto.route;

import lombok.Data;
import org.stupid_talking_potatoes.kis.dto.node.PassingNode;
import org.stupid_talking_potatoes.kis.entity.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * package :  org.stupid_talking_potatoes.kis.route.dto
 * fileName : RealtimeBusLocationResponse
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Data
public class RealtimeBusLocationInfo {
    private String routeId;
    private String routeNo;
    private List<Integer> realtimeNodeOrderList;
    private List<PassingNode> passingNodeList;

    /**
     * Route, PassingNodeList, realtimeNodeOrderList로 RealTimeBusLocationInfo를 만드는 Static Factory Method
     * @param route route
     * @param passingNodeList passingNodeList
     * @param realtimeNodeOrderList realtimeNodeOrderList
     * @return realtimeNodeOrderList
     */
    public static RealtimeBusLocationInfo of(Route route, List<PassingNode> passingNodeList, List<Integer> realtimeNodeOrderList) {
        RealtimeBusLocationInfo realtimeBusLocationInfo = new RealtimeBusLocationInfo();
        realtimeBusLocationInfo.setRealtimeNodeOrderList(realtimeNodeOrderList);
        realtimeBusLocationInfo.setPassingNodeList(passingNodeList);
        realtimeBusLocationInfo.setRouteId(route.getRouteId());
        realtimeBusLocationInfo.setRouteNo(route.getRouteNo());
        return realtimeBusLocationInfo;
    }
}
