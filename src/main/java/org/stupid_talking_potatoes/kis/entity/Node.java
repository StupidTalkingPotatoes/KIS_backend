package org.stupid_talking_potatoes.kis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.stupid_talking_potatoes.kis.dto.tago.TAGO_AroundNodeInfo;

import java.util.HashSet;
import java.util.Set;

/**
 * package :  org.stupid_talking_potatoes.kis.node.domain.base
 * fileName : Node
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Node extends NodeBase {
    private Double longitude;
    private Double latitude;
    
    @OneToMany(mappedBy = "node")
    @ToString.Exclude
    private Set<RouteSeq> routeNodes=new HashSet<>();

    public Node(String nodeId, String nodeName, String nodeNo, Double longitude, Double latitude) {
        super(nodeId, nodeName, nodeNo);
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public static Node of(TAGO_AroundNodeInfo aroundNodeInfo) {
        Node node = new Node(
                aroundNodeInfo.getNodeId(),
                aroundNodeInfo.getNodeNm(),
                aroundNodeInfo.getNodeNo().toString(),
                aroundNodeInfo.getGpsLong(),
                aroundNodeInfo.getGpsLati()
        );
        return node;
    }
}
