package org.stupid_talking_potatoes.kis.entity;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * package :  org.stupid_talking_potatoes.kis.route.domain
 * fileName : Route
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Route extends RouteBase {
    @OneToMany(mappedBy = "nodeId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Node> nodeList;

    private String naverRouteId;
    
    @PostConstruct
    private void init() {
        this.nodeList = builder().nodeList(new ArrayList<>()).build().getNodeList();
    }
}