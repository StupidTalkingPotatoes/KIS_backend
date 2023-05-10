package org.stupid_talking_potatoes.kis.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * package :  org.stupid_talking_potatoes.kis.node.domain.base
 * fileName : NodeBase
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Getter
@SuperBuilder
@MappedSuperclass
@AllArgsConstructor // super로 field set 하기 위해 추가
@NoArgsConstructor
public abstract class NodeBase {
    @Id
    private String nodeId;
    private String nodeName;
    private String nodeNo;
}
