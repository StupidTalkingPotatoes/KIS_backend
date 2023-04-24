package org.stupid_talking_potatoes.kis.route.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stupid_talking_potatoes.kis.route.domain.KneelingBus;

import java.util.Optional;

public interface KneelingBusRepository extends JpaRepository<KneelingBus, String> {
    Optional<KneelingBus> findByBusNo(String busNo);
}