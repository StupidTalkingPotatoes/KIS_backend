package org.stupid_talking_potatoes.kis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

/**
 * package :  org.stupid_talking_potatoes.kis.route.domain
 * fileName : KneelingBus
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Entity
@Getter
public class KneelingBus {
    @Id
    private String vehicleNo;
}
