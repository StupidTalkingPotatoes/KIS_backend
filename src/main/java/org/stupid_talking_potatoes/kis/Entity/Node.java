package org.stupid_talking_potatoes.kis.node.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import org.stupid_talking_potatoes.kis.node.domain.base.NodeBase;

/**
 * package :  org.stupid_talking_potatoes.kis.node.domain.base
 * fileName : Node
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Getter
@Entity
public class Node extends NodeBase {
    private Double longitude;
    private Double latitude;
}