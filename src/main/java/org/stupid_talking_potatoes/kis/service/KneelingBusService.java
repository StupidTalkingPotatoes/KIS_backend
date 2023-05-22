package org.stupid_talking_potatoes.kis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stupid_talking_potatoes.kis.entity.KneelingBus;
import org.stupid_talking_potatoes.kis.repository.KneelingBusRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    /**
     * 저상버스 필터링
     * @param vehicleNo vehicleNo
     * @return 저상버스 여부(boolean)
     */
    public boolean isKneelingBus(String vehicleNo){
        List<KneelingBus> kneelingBusAll = kneelingBusRepository.findAll();
        List<String> vehicleNoList = kneelingBusAll.stream()
                .map(KneelingBus::getVehicleNo)
                .toList();

        return vehicleNoList.contains(vehicleNo);
    }
}
