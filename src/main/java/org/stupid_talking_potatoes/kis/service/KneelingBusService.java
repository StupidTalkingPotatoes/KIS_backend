package org.stupid_talking_potatoes.kis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stupid_talking_potatoes.kis.repository.KneelingBusRepository;


/**
 * package :  org.stupid_talking_potatoes.kis.route.service
 * fileName : KneelingBusService
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Service
public class KneelingBusService {
    @Autowired
    KneelingBusRepository kneelingBusRepository;
    public Boolean isKneelingBus(String busNo){
        return null;
    }
}
