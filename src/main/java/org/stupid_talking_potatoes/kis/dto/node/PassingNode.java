package org.stupid_talking_potatoes.kis.dto.node;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.stupid_talking_potatoes.kis.dto.tago.TAGO_BusLocationInfo;
import org.stupid_talking_potatoes.kis.entity.NodeBase;

import java.math.BigDecimal;


@Getter
@Setter
public class PassingNode {
    private String id;
    private String name;
    private String no;
    private Integer order;
    private Double longitude;
    private Double latitude;

    /**
     * TAGO_BusLocationInfo로 PassingNode를 만드는 Static Factory Method
     * @param busLocationInfo busLocationInfo
     * @return passingNode
     */
    public static PassingNode from(TAGO_BusLocationInfo busLocationInfo){
        PassingNode passingNode = new PassingNode();

        passingNode.setId(busLocationInfo.getNodeId());
        passingNode.setName(busLocationInfo.getNodeNm());
        passingNode.setNo(busLocationInfo.getNodeNo());
        passingNode.setOrder(busLocationInfo.getNodeOrd());
        passingNode.setLongitude(busLocationInfo.getGpsLong());
        passingNode.setLatitude(busLocationInfo.getGpsLati());

        return passingNode;
    }
}
