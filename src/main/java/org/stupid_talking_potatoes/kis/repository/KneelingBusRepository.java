package org.stupid_talking_potatoes.kis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stupid_talking_potatoes.kis.entity.KneelingBus;

import java.util.List;
import java.util.Optional;

public interface KneelingBusRepository extends JpaRepository<KneelingBus, String> {
    List<KneelingBus> findAll();
}