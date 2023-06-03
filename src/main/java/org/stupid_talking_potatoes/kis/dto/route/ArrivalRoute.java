package org.stupid_talking_potatoes.kis.dto.route;

import lombok.*;
import org.stupid_talking_potatoes.kis.entity.RouteBase;

/**
 * package :  org.stupid_talking_potatoes.kis.route.domain
 * fileName : ArrivalRoute
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ArrivalRoute extends RouteBase {
    private Integer prevNodeCnt;
    private Integer arrTime;
    private String departureName; // 객체 생성 시 정해지지 않고 추후 업데이트되는 필드

    @Builder
    public ArrivalRoute(
            String routeId,
            String routeNo,
            Integer prevNodeCnt,
            Integer arrTime
    ) {
        super(routeId, routeNo);
        this.prevNodeCnt = prevNodeCnt;
        this.arrTime = arrTime;
    }
}
