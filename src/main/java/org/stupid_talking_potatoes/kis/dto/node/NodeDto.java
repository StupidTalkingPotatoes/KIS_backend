package org.stupid_talking_potatoes.kis.dto.node;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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