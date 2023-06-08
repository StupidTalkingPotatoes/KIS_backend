package org.stupid_talking_potatoes.kis.repository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * package :  org.stupid_talking_potatoes.kis.repository
 * fileName : RouteRepositoryTest
 * author :  ShinYeaChan
 * date : 2023-04-29
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
class RouteRepositoryTest {
    @Autowired
    RouteRepository routeRepository;
    
}