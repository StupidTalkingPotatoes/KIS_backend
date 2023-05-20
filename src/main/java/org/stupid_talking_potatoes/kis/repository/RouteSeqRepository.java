package org.stupid_talking_potatoes.kis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stupid_talking_potatoes.kis.entity.RouteSeq;

public interface RouteSeqRepository extends JpaRepository<RouteSeq, Long> {
    RouteSeq findByRoute_RouteIdAndNode_NodeId(String routeId, String nodeId);
    
}