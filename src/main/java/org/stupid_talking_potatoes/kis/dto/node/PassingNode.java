package org.stupid_talking_potatoes.kis.dto.node;

import lombok.Getter;
import lombok.Setter;
import org.stupid_talking_potatoes.kis.entity.NodeBase;


@Getter
@Setter
public class PassingNode extends NodeBase {
    private Integer order;
    private String busNo;
}
