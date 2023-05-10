package org.stupid_talking_potatoes.kis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * package :  org.stupid_talking_potatoes.kis.route.domain.base
 * fileName : RouteBase
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Getter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class RouteBase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "routeId")
    private String routeId;

    @Column(name = "routeNo")
    private String routeNo;
}
