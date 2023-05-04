package org.stupid_talking_potatoes.kis.entity;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * package :  org.stupid_talking_potatoes.kis.node.domain.base
 * fileName : Node
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Node extends NodeBase {
    private Double longitude;
    private Double latitude;
}
