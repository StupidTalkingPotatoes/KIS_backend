package org.stupid_talking_potatoes.kis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stupid_talking_potatoes.kis.entity.Node;

import java.util.List;
import java.util.Optional;

/**
 * package :  org.stupid_talking_potatoes.kis.node.repository
 * fileName : NodeRepository
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
public interface NodeRepository extends JpaRepository<Node,String > {
    Optional<Node> findById(String nodeId);
    List<Node> findByNodeNameContaining(String nodeName);
    Optional<Node> findByNodeNo(String nodeNo);
    //Optional<Node> findByNaverId(String naverNodeId);//Node에는 NaberId라는 필드를 갖고 있지않음
}
