package org.stupid_talking_potatoes.kis.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.stupid_talking_potatoes.kis.dto.route.ArrivalRoute;
import org.stupid_talking_potatoes.kis.dto.tago.TAGO_BusArrivalInfo;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class TAGOServiceTest {
    private static TAGOService tagoService;
    @BeforeAll
    public static void set() {
        tagoService = new TAGOService();
    }

    @Nested
    @DisplayName("requestRealtimeBusArrivalInfo 메서드")
    class requestRealtimeBusArrivalInfo {
        @Test
        @DisplayName("requestRealtimeBusArrivalInfo 테스트")
        void requestRealtimeBusArrivalInfoTest() {

        }
    }

    @Nested
    @DisplayName("filterKneelingBus 메서드")
    class filterKneelingBus {
        @Test
        @DisplayName("filterKneelingBus 테스트")
        void filterKneelingBusTest() {
            // given
            ArrayList<TAGO_BusArrivalInfo> items = new ArrayList<>();
            int i = 0;
            for (; i < 3; i++) {
                TAGO_BusArrivalInfo info = new TAGO_BusArrivalInfo(
                        "id"+i,
                        "name"+i,
                        String.valueOf(i),
                        String.valueOf(i),
                        "마을버스",
                        i*5,
                        "저상버스",
                        i*5*2
                );
                items.add(info);
            }
            TAGO_BusArrivalInfo info = new TAGO_BusArrivalInfo(
                    "id"+i,
                    "name"+i,
                    String.valueOf(i),
                    String.valueOf(i),
                    "좌석버스",
                    i*5,
                    "일반차량",
                    i*5*2
            );
            items.add(info);

            // when
            ArrayList<ArrivalRoute> arrivalRoutes = tagoService.filterBusArrivalInfo(items);

            // then
            assertEquals(3, arrivalRoutes.size());

        }

    }
}
