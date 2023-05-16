package org.stupid_talking_potatoes.kis.dto.tago;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class TAGO_Response<T> {
    Response<T> response;
}

@NoArgsConstructor
class Response<T> {
    Header header;
    Body<T> body;
}

@NoArgsConstructor
class Header {
    String resultCode;
    String resultMsg;
}

@NoArgsConstructor
class Body<T> {
    T items;
    int numOfRows;
    int pageNo;
    int totalCount;
}