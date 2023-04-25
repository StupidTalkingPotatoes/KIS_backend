package org.stupid_talking_potatoes.kis.naver.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Context{
    public String engineVersion;
    public Object engineUrl;
    public String start;
    public String goal;
    public Date departureTime;
    public Date currentDateTime;
    public ServiceDay serviceDay;
    public String recommendDirectionsType;
    public Object uuid;
}
