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
public class ArrivalRoute extends RouteBase implements Comparable<ArrivalRoute> {
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

    @Override
    public int compareTo(ArrivalRoute r) {
        // 주의: overflow가 일어날 수 있으므로 빼기 연산을 쓰는 것은 위험함
        //      하지만 본 application에서 arrTime이 10만 이상을 넘기지 않을 것.
        return this.getArrTime() - r.getArrTime();
    }
}
