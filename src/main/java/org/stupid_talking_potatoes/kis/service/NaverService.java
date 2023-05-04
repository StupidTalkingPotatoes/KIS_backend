package org.stupid_talking_potatoes.kis.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
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
        JSONArray paths = ptMapJSON.getJSONObject("res").getJSONArray("paths");
        ArrayList<Path> pathList=new ArrayList<>();
        setPathProperty(paths,pathList);
        return pathList;
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
        return baseUrl + "?" +
                "&mode=TIME" +
                "&departureTime=" + LocalDateTime.now() +
                "&departure=" + departureLongitude + "," + departureLatitude +
                "&arrival=" + arrivalLongitude + "," + arrivalLatitude;
    }
    
    void setPathProperty(JSONArray paths, ArrayList<Path> pathDtos) {
        for (int i=0;i<paths.length();i++) {
            Object path=paths.get(i);
            JSONObject pathObj = (JSONObject) path;
            
            Integer duration = Integer.parseInt(pathObj.get("duration").toString());
            pathDtos.get(i).setDuration(duration);
            
            JSONArray legs = pathObj.getJSONArray("legs");
            appendLegs(legs,pathDtos.get(i).getStepList());
        }
    }
    
    void appendLegs(JSONArray legs, ArrayList<Step> stepList) {
        for (Object leg : legs) {
            JSONObject legObj = (JSONObject) leg;
            JSONArray steps = legObj.getJSONArray("steps");
            setStepProperties(steps,stepList);
        }
    }
    
    
    void setStepProperties(JSONArray steps, ArrayList<Step> stepList){
        for (int i=0;i<steps.length();i++) {
            Object step=steps.get(i);
            JSONObject stepObj = (JSONObject) step;
            String stepType = stepObj.get("type").toString();
            
            if (stepType.equals("BUS")) {
                Integer duration = Integer.parseInt(stepObj.get("duration").toString());
                String departureTime = stepObj.get("departureTime").toString();
                String arrivalTime = stepObj.get("arrivalTime").toString();
                stepList.get(i).setType(stepType);
                stepList.get(i).setDuration(duration);
                stepList.get(i).setArrival(arrivalTime);
                stepList.get(i).setDeparture(departureTime);
                JSONArray stations=stepObj.getJSONArray("stations");
                setArrivalRouteProperties(stations,stepList.get(i).getArrivalRouteList());
            }
        }
    }
    
    void setArrivalRouteProperties(JSONArray stations, ArrayList<ArrivalRoute> arrivalRouteList) {
        for (int i=0;i<stations.length();i++) {
            Object station=stations.get(i);
            JSONObject stationObj = (JSONObject) station;
            //TODO: ArrivalRoute 받아와서 Path 마무리짓기
//            arrivalRouteList.get(i).setPrevNodeCnt();
//            arrivalRouteList.get(i).setArrTime();
//            arrivalRouteList.get(i).setDepartureName();
        }
    }
    private  ArrayList<Path> filterPath(List<ArrivalRoute> arrivalRouteList){
        return null;
    }
}
