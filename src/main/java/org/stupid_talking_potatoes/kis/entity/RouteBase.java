package org.stupid_talking_potatoes.kis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class RouteBase {
    @Id
    private String routeId;
    private String routeNo;
}
