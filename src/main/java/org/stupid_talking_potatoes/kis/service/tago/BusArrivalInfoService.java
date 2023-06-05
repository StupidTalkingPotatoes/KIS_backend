package org.stupid_talking_potatoes.kis.service.tago;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.stupid_talking_potatoes.kis.config.TagoServiceConfig;
import org.stupid_talking_potatoes.kis.dto.route.ArrivalRoute;
import org.stupid_talking_potatoes.kis.dto.tago.TAGO_BusArrivalInfo;

import java.util.ArrayList;
import java.util.List;

@Service
public class BusArrivalInfoService extends TagoBaseService<TAGO_BusArrivalInfo, ArrivalRoute> {

    public BusArrivalInfoService(TagoServiceConfig config) {
        super(config);
    }

    private String buildUri(String nodeId) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("apis.data.go.kr")
                .path("/1613000/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList")
                .queryParam("serviceKey", super.getServiceKey())
                // if there is error, return type is always xml.
                .queryParam("_type", "xml")
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 100)
                .queryParam("cityCode", super.getCityCode())
                .queryParam("nodeId", nodeId)
                .build();
        return uriComponents.toString();
    }

    public List<ArrivalRoute> requestRealtimeBusArrivalInfo(String nodeId) {
        String url = this.buildUri(nodeId);
        JSONObject responseBody = super.request(url);
        List<TAGO_BusArrivalInfo> list = super.convert(responseBody.toString(), new TypeReference<>(){}, new TypeReference<>() {});
        return this.filter(list);
    }

    protected List<ArrivalRoute> filter(List<TAGO_BusArrivalInfo> list) {
        List<ArrivalRoute> arrivalRoutes = new ArrayList<>();

        for (TAGO_BusArrivalInfo busArrivalInfo: list) {
            // check kneeling bus
            if (busArrivalInfo.getVehicleTp().equals("저상버스")) {
                // convert time from sec to min
                int arrTimeSec = busArrivalInfo.getArrTime();
                int arrTimeMin = this.convertSecToMin(arrTimeSec);
                // build object and add to list
                arrivalRoutes.add(
                        ArrivalRoute.builder()
                                .routeId(busArrivalInfo.getRouteId())
                                .routeNo(busArrivalInfo.getRouteNo())
                                .prevNodeCnt(busArrivalInfo.getArrPrevStationCnt())
                                .arrTime(arrTimeMin)
                                .build()
                );
            }
        }
        return arrivalRoutes;
    }
}
