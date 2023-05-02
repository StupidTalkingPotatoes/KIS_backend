package org.stupid_talking_potatoes.kis.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * package :  org.stupid_talking_potatoes.kis.route.domain.base
 * fileName : RouteBase
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class RouteBase {
    @Id
    private String routeId;
    private String routeNo;

}
