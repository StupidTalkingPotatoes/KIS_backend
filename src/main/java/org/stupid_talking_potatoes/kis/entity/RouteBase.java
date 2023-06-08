package org.stupid_talking_potatoes.kis.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * package :  org.stupid_talking_potatoes.kis.route.domain.base
 * fileName : RouteBase
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Getter
@Setter
@ToString
@SuperBuilder
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public abstract class RouteBase {
    @Id
    private String routeId;
    private String routeNo;
}
