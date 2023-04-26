package org.stupid_talking_potatoes.kis.route.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link org.stupid_talking_potatoes.kis.node.domain.Node} entity
 */
@Data
@Embeddable
@NoArgsConstructor
public class NodeDto implements Serializable {
    private String id;
    private String name;
    private String no;
    private Double longitude;
    private Double latitude;
}