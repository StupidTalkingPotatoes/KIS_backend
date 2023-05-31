package org.stupid_talking_potatoes.kis.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.stupid_talking_potatoes.kis.dto.node.NodeDto;
import org.stupid_talking_potatoes.kis.dto.node.RealtimeBusArrivalInfo;
import org.stupid_talking_potatoes.kis.dto.route.ArrivalRoute;
import org.stupid_talking_potatoes.kis.entity.Node;
import org.stupid_talking_potatoes.kis.repository.NodeRepository;
import org.stupid_talking_potatoes.kis.service.tago.AroundNodeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class NodeServiceTest {
    private static NodeService nodeService;
    private static NodeRepository nodeRepository;
    private static RouteService routeService;
    private static TAGOService tagoService;
    private static AroundNodeService aroundNodeService;
    
    @BeforeAll
    public static void set() {
        nodeRepository = Mockito.mock(NodeRepository.class);
        tagoService = Mockito.mock(TAGOService.class);
        routeService = Mockito.mock(RouteService.class);
        aroundNodeService = Mockito.mock(AroundNodeService.class);
        nodeService = new NodeService(nodeRepository, tagoService, routeService, aroundNodeService);
    }
    
    @Nested
    @DisplayName("getNodeList 메서드")
    class getNodeList {
        @Test
        @DisplayName("getNodeList 테스트 - name으로 검색 - 성공")
        void getNodeByName() {
            // given
            String name = "옥계중학교";
            Node node1 = new Node("1", "옥계중학교건너", "1", 1.1, 1.1);
            Node node2 = new Node("2", "옥계중학교앞", "2", 1.1, 1.1);
            
            ArrayList<Node> nodes = new ArrayList<Node>();
            nodes.add(node1); nodes.add(node2);;
            
            when(nodeRepository.findByNodeNameContaining(name)).thenReturn(nodes);
            
            // when
            List<NodeDto> response = nodeService.getNodeList(null, name);
            
            // then
            assertEquals(2, response.size());
        }
        
        @Test
        @DisplayName("getNodeList 테스트 - no로 검색 - 성공")
        void getNodeByNo() {
            // given
            String no = "10131";
            Node node = new Node("1", "옥계중학교건너", "10131", 1.1, 1.1);
            
            when (nodeRepository.findByNodeNo(no)).thenReturn(Optional.of(node));
            
            // when
            List<NodeDto> response = nodeService.getNodeList(no, null);
            
            // then
            assertEquals(1, response.size());
            assertEquals("10131", response.get(0).getNo());
        }
    }
    
    @Nested
    @DisplayName("getRealtimeBusArrivalInfo 메서드")
    class getRealtimeBusArrivalInfo {
        @Test
        @DisplayName("getRealtimeBusArrivalInfo 테스트 - 성공")
        void getInfoById() {
            // given
            String id = "GMB131";
            Node node = new Node("GMB131", "옥계중학교건너", "10131", 1.1, 1.1);
            ArrivalRoute arrivalRoute1 = new ArrivalRoute("1", "190", 3, 180);
            ArrivalRoute arrivalRoute2 = new ArrivalRoute("2", "195", 10, 600);
            
            ArrayList<ArrivalRoute> routes = new ArrayList<ArrivalRoute>();
            routes.add(arrivalRoute1); routes.add(arrivalRoute2);
            
            when (nodeRepository.findById(id)).thenReturn(Optional.of(node));
            when (tagoService.requestRealtimeBusArrivalInfo(id)).thenReturn(routes);
            when (routeService.getArrivalName(any(String.class))).thenReturn("구미역");
            
            // when
            RealtimeBusArrivalInfo response = nodeService.getRealtimeBusArrivalInfo(id);
            
            // then
            assertEquals(2, response.getArrivalRouteList().size());
            assertEquals(180, response.getArrivalRouteList().get(0).getArrTime()); // sorting test
        }
        
        @Test
        @DisplayName("getRealtimeBusArrivalInfo 테스트 - 실패 (Invalid ID)")
        void getInfoWithInvalidId() {
        
        }
    }
    
    @Nested
    @DisplayName("getAroundNodeInfo 메서드")
    class getAroundNodeInfo {
        @Test
        @DisplayName("getAroundNodeInfo 테스트 - 성공 (주변 정류장이 있는 경우)")
        void getNodes() {
            // given
            Double latitude = 36.140382;
            Double longitude = 128.3964849;
            
            ArrayList<Node> nodes = new ArrayList<>();
            nodes.add(new Node("GMB131", "금오공대입구(옥계중학교방면)", "10131", 128.3967393, 36.13948442));
            nodes.add(new Node("GMB130", "금오공대입구(금오공대종점방면)", "10130", 128.3971135, 36.13929449));
            nodes.add(new Node("GMB132", "금오공대종점", "10132", 128.394333, 36.142569));
            
            when (tagoService.requestAroundNodeInfo(longitude, latitude)).thenReturn(nodes);
            
            // when
            List<NodeDto> response = nodeService.getAroundNodeInfo(longitude, latitude);
            
            // then
            assertEquals(3, response.size());
        }
        
        @Test
        @DisplayName("getAroundNodeInfo 테스트 - 성공 (주변 정류장이 없는 경우)")
        void noAroundNode() {
            // given
            Double latitude = 36.1702;
            Double longitude = 128.3903;
            
            ArrayList<Node> nodes = new ArrayList<>();
            
            when (tagoService.requestAroundNodeInfo(longitude, latitude)).thenReturn(nodes);
            
            // when
            List<NodeDto> response = nodeService.getAroundNodeInfo(longitude, latitude);
            
            // then
            assertEquals(0, response.size());
        }
    }
}