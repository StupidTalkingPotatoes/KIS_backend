package org.stupid_talking_potatoes.kis.route.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import lombok.Getter;
import org.stupid_talking_potatoes.kis.route.domain.base.RouteBase;

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
    @ElementCollection
    private List<NodeDto> nodeList;//Node entity 자체가 DB에서 연관을 지을거같지 않아서 DTO로 대체
}
