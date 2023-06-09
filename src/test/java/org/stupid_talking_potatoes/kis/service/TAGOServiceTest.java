package org.stupid_talking_potatoes.kis.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.stupid_talking_potatoes.kis.dto.route.ArrivalRoute;
import org.stupid_talking_potatoes.kis.dto.tago.TAGO_AroundNodeInfo;
import org.stupid_talking_potatoes.kis.dto.tago.TAGO_BusArrivalInfo;
import org.stupid_talking_potatoes.kis.entity.Node;

import java.io.UnsupportedEncodingException;
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
    @DisplayName("convertSecToMin 메서드")
    class convertSecToMin {
        @Test
        @DisplayName("convertSecToMin 테스트 - 성공")
        void convertSetToMin() {
            assertEquals(10, tagoService.convertSecToMin(600));
            assertEquals(10, tagoService.convertSecToMin(620));
            assertEquals(11, tagoService.convertSecToMin(640));
        }
    }
    
    @Nested
    @DisplayName("convert 메서드")
    class convert {
        @Test
        @DisplayName("convert 테스트 - 성공 (resultCode가 00인 경우)")
        void convert() {
            // given
            String jsonBody = "{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\": {\"item\": [{\"nodeid\":\"1\",\"nodenm\":\"name\",\"routeid\":\"1\",\"routetp\":\"마을버스\",\"arrprevstationcnt\":5,\"vehicletp\":\"저상버스\",\"arrtime\":5}]},\"numOfRows\":100,\"pageNo\":1,\"totalCount\":1}}}";
            
            // when
            ArrayList<TAGO_BusArrivalInfo> list = tagoService.convertArrivalInfo(jsonBody);
            
            // then
            assertEquals(1, list.size());
        }
        
        @Test
        @DisplayName("convert 테스트 - 성공 (items가 빈 경우)")
        void convertWithNoItem() {
            // given
            String jsonBody = "{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":100,\"pageNo\":1,\"totalCount\":0}}}";
            
            // when
            ArrayList<TAGO_BusArrivalInfo> list = tagoService.convertArrivalInfo(jsonBody);
            
            // then
            assertEquals(0, list.size());
        }
        
        @Test
        @DisplayName("convert 테스트 - 실패 (resultCode가 00이 아닌 경우)")
        void convertWithErrorResult() {
            // given
            String jsonBody = "{\"response\":{\"header\":{\"resultCode\":\"30\",\"resultMsg\":\"SERVICE_KEY_IS_NOT_REGISTERED_ERROR.\"},\"body\":{\"items\":[{\"nodeid\":\"1\",\"nodenm\":\"name\",\"routeid\":\"1\",\"routetp\":\"마을버스\",\"arrprevstationcnt\":5,\"vehicletp\":\"저상버스\",\"arrtime\":5}],\"numOfRows\":100,\"pageNo\":1,\"totalCount\":1}}}";
            
            // when & then
            RuntimeException exception = assertThrows(RuntimeException.class, () -> tagoService.convertArrivalInfo(jsonBody));
        }
    }
    
    @Nested
    @DisplayName("filterKneelingBus 메서드")
    class filterKneelingBus {
        @Test
        @DisplayName("filterKneelingBus 테스트")
        void filterKneelingBusTest() throws UnsupportedEncodingException {
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
                        new String( "저상버스".getBytes("utf-8"), "iso-8859-1"),
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
                    new String( "일반차량".getBytes("utf-8"), "iso-8859-1"),
                    i*5*2
            );
            items.add(info);
            
            // when
            ArrayList<ArrivalRoute> arrivalRoutes = tagoService.filterBusArrivalInfo(items);
            
            // then
            assertEquals(3, arrivalRoutes.size());
            
        }
        
    }
    
    @Nested
    @DisplayName("filterArroundNodeInfo 메서드")
    class filterArroundNodeInfo {
        @Test
        @DisplayName("filterArroundNodeInfo 테스트")
        void filterArroundNodeInfo() {
            // given
            ArrayList<TAGO_AroundNodeInfo> aroundNodeList = new ArrayList<>();
            aroundNodeList.add(new TAGO_AroundNodeInfo(37050, 36.13948442, 128.3967393,"GMB131", "금오공대입구(옥계중학교방면)", 10131));
            aroundNodeList.add(new TAGO_AroundNodeInfo(37390, 36.13948442, 128.3967393,"GMB131", "금오공대입구(옥계중학교방면)", 10131));
            aroundNodeList.add(new TAGO_AroundNodeInfo(37050, 36.13948442, 128.3967393,"GMB131", "금오공대입구(옥계중학교방면)", 10131));
            aroundNodeList.add(new TAGO_AroundNodeInfo(37390, 36.13948442, 128.3967393,"GMB131", "금오공대입구(옥계중학교방면)", 10131));
            
            // when
            ArrayList<Node> nodeList = tagoService.filterArroundNodeInfo(aroundNodeList);
            
            // then
            assertEquals(2, nodeList.size());
        }
    }
}