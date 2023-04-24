package org.stupid_talking_potatoes.kis.node.domain.dto;

import lombok.Data;
import org.stupid_talking_potatoes.kis.node.domain.base.NodeBase;


@Data
public class PassingNode extends NodeBase {
    private Integer order;
    private String busNo;
}
