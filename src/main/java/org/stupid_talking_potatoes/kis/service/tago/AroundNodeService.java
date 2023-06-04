package org.stupid_talking_potatoes.kis.service.tago;

import com.fasterxml.jackson.core.type.TypeReference;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.stupid_talking_potatoes.kis.config.TagoServiceConfig;
import org.stupid_talking_potatoes.kis.dto.tago.TAGO_AroundNodeInfo;
import org.stupid_talking_potatoes.kis.entity.Node;

import java.util.ArrayList;
import java.util.List;

@Service
public class AroundNodeService extends TagoBaseService<TAGO_AroundNodeInfo, Node> {

    public AroundNodeService(TagoServiceConfig config) {
        super(config);
    }

    private String buildUri(Double longitude, Double latitude) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("apis.data.go.kr")
                .path("/1613000/BusSttnInfoInqireService/getCrdntPrxmtSttnList")
                .queryParam("serviceKey", super.getServiceKey())
                // if there is error, return type is always xml.
                .queryParam("_type", "xml")
                .queryParam("pageNo", 1)
                .queryParam("numOfRows",100)
                .queryParam("gpsLong", longitude)
                .queryParam("gpsLati", latitude)
                .build();
        return uriComponents.toString();
    }

    public List<Node> requestAroundNodeInfo(Double longitude, Double latitude) {
        String url = this.buildUri(longitude, latitude);
        JSONObject responseBody = super.request(url);
        List<TAGO_AroundNodeInfo> list = super.convert(responseBody.toString(), new TypeReference<>(){}, new TypeReference<>() {});
        return this.filter(list);
    }

    protected List<Node> filter(List<TAGO_AroundNodeInfo> list) {
        List<Node> nodeList = new ArrayList<>();

        for (TAGO_AroundNodeInfo arrivalInfo: list) {
            // filter by city code
            if (arrivalInfo.getCityCode().equals(super.getCityCode())) {
                // encode name to utf-8
                arrivalInfo.setNodeNm(super.encodeToUTF8(arrivalInfo.getNodeNm()));
                nodeList.add(Node.of(arrivalInfo));
            }
        }
        return nodeList;
    }

}
