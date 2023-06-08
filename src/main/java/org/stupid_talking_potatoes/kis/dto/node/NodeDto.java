package org.stupid_talking_potatoes.kis.dto.node;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.stupid_talking_potatoes.kis.entity.Node;

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

    @Builder
    public NodeDto(Node node) {
        this.id = node.getNodeId();
        this.name = node.getNodeName();
        this.no = node.getNodeNo();
        this.longitude = node.getLongitude();
        this.latitude = node.getLatitude();
    }
}