package org.stupid_talking_potatoes.kis.dto.route;

import lombok.Getter;
import lombok.Setter;
import org.stupid_talking_potatoes.kis.entity.Route;
import org.stupid_talking_potatoes.kis.entity.RouteBase;

/**
 * package :  org.stupid_talking_potatoes.kis.route.domain.base
 * fileName : SearchedRoute
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Getter
@Setter
public class SearchedRoute extends RouteBase {
    private String startNode;
    private String endNode;

    /**
     * RouteEntity -> SearchedRoute (Static Factory Method)
     * @param route RouteEntity
     * @return SearchedRoute
     */
    public static SearchedRoute fromEntity(Route route) {
        SearchedRoute searchedRoute = new SearchedRoute();
        searchedRoute.setRouteId(route.getRouteId());
        searchedRoute.setRouteNo(route.getRouteNo());
        searchedRoute.setStartNode(route.getStartNodeName());
        searchedRoute.setEndNode(route.getEndNodeName());
        return searchedRoute;
    }
}
