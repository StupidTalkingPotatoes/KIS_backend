package org.stupid_talking_potatoes.kis.dto.tago;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class TAGO_ApiResponse<T> {
    Header header;
    Body<T> body;

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Header {
        String resultCode;
        String resultMsg;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Body<T> {
        Items<T> items;
        int numOfRows;
        int pageNo;
        int totalCount;

        @NoArgsConstructor
        @Getter
        @Setter
        public static class Items<T> {
            List<T> item;
        }
    }

}
