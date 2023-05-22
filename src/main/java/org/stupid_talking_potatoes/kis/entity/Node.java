package org.stupid_talking_potatoes.kis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

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
@NoArgsConstructor
public class Node extends NodeBase {
    private Double longitude;
    private Double latitude;
    
    @OneToMany(mappedBy = "node")
    @ToString.Exclude
    private Set<RouteSeq> routeNodes;

    public Node(String nodeId, String nodeName, String nodeNo, Double longitude, Double latitude) {
        super(nodeId, nodeName, nodeNo);
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
