package org.stupid_talking_potatoes.kis.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * package :  org.stupid_talking_potatoes.kis.route.domain
 * fileName : Route
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class Route extends RouteBase {
    private String startNodeName;
    private String endNodeName;
    @OneToMany(mappedBy = "route")
    @ToString.Exclude
    private Set<RouteSeq> routeNodes = new HashSet<>();
}