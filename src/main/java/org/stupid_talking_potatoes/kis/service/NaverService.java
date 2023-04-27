package org.stupid_talking_potatoes.kis.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.stupid_talking_potatoes.kis.dto.path.Path;
import org.stupid_talking_potatoes.kis.dto.path.Step;
import org.stupid_talking_potatoes.kis.dto.route.ArrivalRoute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * package :  org.stupid_talking_potatoes.kis.naver.service
 * fileName : NaverService
 * author :  ShinYeaChan
 * date : 2023-04-23
 */
@Service
@Slf4j
public class NaverService {
    public ArrayList<Path> getTransportationInfo(Double departureLongitude, Double departureLatitude, Double arrivalLongitude, Double arrivalLatitude){
        JSONObject ptMapJSON = getPtMapJSON(departureLongitude, departureLatitude, arrivalLongitude, arrivalLatitude);
        return jsonToDto(ptMapJSON);
    }
    
    private ArrayList<Path> jsonToDto(JSONObject json) {
        int jsonLength=json.getJSONObject("res").getJSONArray("paths").length();
        for (int i = 0; i < jsonLength; i++) {
            Path path=new Path();
            path.setDuration((Integer) json.getJSONObject("res").getJSONArray("paths").getJSONObject(0).get("duration"));
            Step step=new Step();
            json.getJSONObject("res").getJSONArray("paths").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps").getJSONObject(0).toString();
            path.setStepList(null);
        }
        return null;
    }
    
    public JSONObject getPtMapJSON(Double departureLongitude, Double departureLatitude, Double arrivalLongitude, Double arrivalLatitude) {
        String url = buildUrl(departureLongitude, departureLatitude, arrivalLongitude, arrivalLatitude);
        try {
            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            log.info("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            return new JSONObject(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private String buildUrl(Double departureLongitude, Double departureLatitude, Double arrivalLongitude, Double arrivalLatitude) {
        String baseUrl = "https://pt.map.naver.com/api/pubtrans-search";
        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        urlBuilder.append("?");
        urlBuilder.append("&mode=TIME");
        urlBuilder.append("&departureTime=").append(LocalDateTime.now());
        urlBuilder.append("&departure=").append(departureLongitude).append(",").append(departureLatitude);
        urlBuilder.append("&arrival=").append(arrivalLongitude).append(",").append(arrivalLatitude);
        return urlBuilder.toString();
    }

    private  ArrayList<Path> filterPath(List<ArrivalRoute> arrivalRouteList){
        return null;
    }
}
