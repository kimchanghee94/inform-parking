package com.inpark.controller;

import com.inpark.service.ParkingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ParkingController {
    @Autowired
    private ParkingService parkingService;

    @RequestMapping(value="/getMapViewMarkers", method=RequestMethod.POST)
    @ResponseBody
    public String getMapViewMarkers(String neLat, String neLng, String swLat, String swLng){
        return parkingService.combSelectParking(neLat, neLng, swLat, swLng);
    }
}
