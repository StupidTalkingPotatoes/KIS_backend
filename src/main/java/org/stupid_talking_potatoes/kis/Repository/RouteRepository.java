package org.stupid_talking_potatoes.kis.route.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stupid_talking_potatoes.kis.route.domain.Route;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, String> {
    Optional<Route> findById(String routeId);
    List<Route> findByNoContaining(String routeNo);
    Optional<Route> findByNaverId(String naverRouteId);//Route에 NaberId라는 필드를 갖고 있지않음
}