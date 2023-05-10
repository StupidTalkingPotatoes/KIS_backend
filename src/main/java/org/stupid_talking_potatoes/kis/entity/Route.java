package org.stupid_talking_potatoes.kis.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

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
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route extends RouteBase{
    private String naverRouteId;
    
    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Node> nodeList = new ArrayList<>();
}