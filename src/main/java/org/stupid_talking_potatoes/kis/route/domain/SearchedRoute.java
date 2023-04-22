package org.stupid_talking_potatoes.kis.route.domain;

import lombok.Data;
import org.stupid_talking_potatoes.kis.route.domain.base.RouteBase;

/**
 * package :  org.stupid_talking_potatoes.kis.route.domain.base
 * fileName : SearchedRoute
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Data
public class SearchedRoute extends RouteBase {
    private String departure;
    private String arrival;
}
