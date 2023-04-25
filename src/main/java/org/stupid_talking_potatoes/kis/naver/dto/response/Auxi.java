package org.stupid_talking_potatoes.kis.naver.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * package :  org.stupid_talking_potatoes.kis.naver.dto.response
 * fileName : Auxi
 * author :  ShinYeaChan
 * date : 2023-04-25
 */
@Getter
@Setter
public class Auxi {
        public Platform platform;
        public Crossover crossover;
        public Door door;
        public Restroom restroom;
        public ArrayList<String> facilities;
        public ArrayList<String> disabledFacilities;
        public LostCenter lostCenter;
}