package org.stupid_talking_potatoes.kis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.stupid_talking_potatoes.kis.entity.Route;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, String> {
    Optional<Route> findByRouteId(String routeId);
    
    List<Route> findByRouteNo(@NonNull String routeNo);
    
}