package org.stupid_talking_potatoes.kis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

/**
 * package :  org.stupid_talking_potatoes.kis.node.domain.base
 * fileName : NodeBase
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Getter
@Entity
@MappedSuperclass
public class NodeBase {
    @Id
    private String id;
    private String name;
    private String no;
}
