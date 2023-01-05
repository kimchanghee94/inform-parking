package com.inpark.controller;

import com.inpark.dto.CompParkingDto;
import com.inpark.dto.ParkingDto;
import com.inpark.service.ParkingService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ParkingController {
    @Autowired
    private ParkingService parkingService;

    @RequestMapping(value="/getMapViewMarkers", method=RequestMethod.POST)
    @ResponseBody
    public String getMapViewMarkers(String neLat, String neLng, String swLat, String swLng){
        Double dneLat = Double.parseDouble(neLat);
        Double dneLng = Double.parseDouble(neLng);
        Double dswLat = Double.parseDouble(swLat);
        Double dswLng = Double.parseDouble(swLng);

        CompParkingDto compParkingDto = new CompParkingDto();
        compParkingDto.setNeLat(dneLat);
        compParkingDto.setNeLng(dneLng);
        compParkingDto.setSwLat(dswLat);
        compParkingDto.setSwLng(dswLng);

        List<ParkingDto> listParking = parkingService.selectViewParking(compParkingDto);

        /*json작업 시작*/
        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        JSONArray items = new JSONArray();

        for(int i=0; i<listParking.size(); i++){
            JSONObject temp = new JSONObject();
            temp.put("parkingName", listParking.get(i).getParkingName());
            temp.put("latitude", listParking.get(i).getLatitude());
            temp.put("longitude", listParking.get(i).getLongitude());

            items.add(temp);
        }

        body.put("items", items);
        root.put("body", body);

        if(listParking.size() == 0){
            header.put("statusCode", "01");
            System.out.println("값을 받은게 존재하지 않는다.");
        }else{
            header.put("statusCode", "00");
            System.out.println("정상적으로 값을 받아옴");
        }
        root.put("header", header);

        System.out.println(root.toString());

        return root.toJSONString();
    }
}
