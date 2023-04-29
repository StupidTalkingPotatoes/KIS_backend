package org.stupid_talking_potatoes.kis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.List;

/**
 * package :  org.stupid_talking_potatoes.kis.route.domain
 * fileName : Route
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Entity
@Getter
public class Route extends RouteBase {
    @OneToMany
    private List<Node> nodeList;
    private String naverRouteId;
}
