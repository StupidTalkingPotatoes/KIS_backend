package org.stupid_talking_potatoes.kis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.stupid_talking_potatoes.kis.entity.Route;
import org.stupid_talking_potatoes.kis.entity.RouteSeq;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, String> {
    List<Route> findByRouteNo(@NonNull String routeNo);

    /**
     * 검색하고자 하는 routeNo를 사용하여 RouteNo 기준으로 Like 검색
     * @param routeNo routeNo 검색어
     * @return routeNo가 포함된 RouteList
     */
    List<Route> findAllByRouteNoContaining(String routeNo);

    Optional<Route> findByRouteId(String routeId);
}