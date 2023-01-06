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
            temp.put("phone", listParking.get(i).getPhone());

            temp.put("parkingFor", listParking.get(i).getParkingFor());
            temp.put("parkingStruct", listParking.get(i).getParkingStruct());
            temp.put("rdnmadr", listParking.get(i).getRdnmadr());
            temp.put("lnmadr", listParking.get(i).getLnmadr());
            temp.put("openDay", listParking.get(i).getOpenDay());
            temp.put("parkingCnt", listParking.get(i).getParkingCnt());

            temp.put("weekOpen", listParking.get(i).getWeekOpen());
            temp.put("weekClose", listParking.get(i).getWeekClose());
            temp.put("satOpen", listParking.get(i).getSatOpen());
            temp.put("satClose", listParking.get(i).getSatClose());
            temp.put("holiOpen", listParking.get(i).getHoliOpen());
            temp.put("holiClose", listParking.get(i).getHoliClose());

            temp.put("parkingFreeInfo", listParking.get(i).getParkingFreeInfo());
            temp.put("basicTime", listParking.get(i).getBasicTime());
            temp.put("basicCharge", listParking.get(i).getBasicCharge());
            temp.put("addUnitTime", listParking.get(i).getAddUnitTime());
            temp.put("addUnitCharge", listParking.get(i).getAddUnitCharge());
            temp.put("dayChargeTime", listParking.get(i).getDayChargeTime());
            temp.put("dayCharge", listParking.get(i).getDayCharge());
            temp.put("monthCharge", listParking.get(i).getMonthCharge());

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
