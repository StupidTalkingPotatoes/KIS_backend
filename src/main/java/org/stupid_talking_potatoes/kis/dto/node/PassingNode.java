package org.stupid_talking_potatoes.kis.dto.node;

import lombok.Getter;
import lombok.Setter;
import org.stupid_talking_potatoes.kis.entity.RouteSeq;


@Getter
@Setter
public class PassingNode {
    private String id;
    private String name;
    private String no;
    private Integer order;
    private Double longitude;
    private Double latitude;

    /**
     * routeSeq(Entity)로 PassingNode를 만드는 Static Factory Method
     * @param routeSeq routeSeq
     * @return passingNode
     */
    public static PassingNode fromRouteSeq(RouteSeq routeSeq){
        PassingNode passingNode = new PassingNode();

        passingNode.setId(routeSeq.getNode().getNodeId());
        passingNode.setName(routeSeq.getNode().getNodeName());
        passingNode.setNo(routeSeq.getNode().getNodeNo());
        passingNode.setOrder(routeSeq.getNodeOrder());
        passingNode.setLongitude(routeSeq.getNode().getLongitude());
        passingNode.setLatitude(routeSeq.getNode().getLatitude());

        return passingNode;
    }
}
