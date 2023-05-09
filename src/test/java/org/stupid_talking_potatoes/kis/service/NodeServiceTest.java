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
import org.stupid_talking_potatoes.kis.entity.Node;
import org.stupid_talking_potatoes.kis.repository.NodeRepository;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class NodeServiceTest {
    private static NodeService nodeService;
    private static NodeRepository nodeRepository;

    @BeforeAll
    public static void set() {
        nodeRepository = Mockito.mock(NodeRepository.class);
        nodeService = new NodeService(nodeRepository);
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
            Node node3 = new Node("3", "옥계중중중", "3", 1.1, 1.1);
            nodeRepository.save(node1);
            nodeRepository.save(node2);
            nodeRepository.save(node3);

            // when
            ArrayList<NodeDto> nodes = nodeService.getNodeList(null, name);

            // then
            assertEquals(2, nodes.size());
        }

        @Test
        @DisplayName("getNodeList 테스트 - no로 검색 - 성공")
        void getNodeByNo() {
            // given

            // when
            // then
        }
    }
}