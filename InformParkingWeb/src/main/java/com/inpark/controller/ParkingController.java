package com.inpark.controller;

import com.inpark.service.ParkingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @RequestMapping(value="/getParkingAuthCheck", method=RequestMethod.POST)
    @ResponseBody
    public String getParkingAuthCheck(String parkingNo, String referenceDate){
        System.out.println("parkingNo : " + parkingNo + "referenceDate : " + referenceDate);
        return parkingService.selectAuthParkingAdmin(parkingNo, referenceDate);
    }

    @RequestMapping(value="/addDBAdminRolePage", method=RequestMethod.POST)
    @ResponseBody
    public String addDBAdminRolePage(String parkingNo, String referenceDate) throws Exception{
        return parkingService.insertAdminParking(parkingNo, referenceDate);
    }

    @RequestMapping(value="/deleteAdminParkingField", method=RequestMethod.POST)
    @ResponseBody
    public String deleteAdminParkingField(String parkingNo, String referenceDate) throws Exception{
        return parkingService.deleteAdminParkingField(parkingNo, referenceDate);
    }

    @RequestMapping(value="/getInitSettingAdmin", method=RequestMethod.POST)
    @ResponseBody
    public String getInitSettingAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String id = userDetails.getUsername();

        return parkingService.selectAdminParkingList(id);
    }

    @RequestMapping(value="/updateParkingUseCnt", method=RequestMethod.POST)
    @ResponseBody
    public String insertParkingUseCnt(String parkingNo, String referenceDate, int parkingUseCnt) throws Exception {
        return parkingService.updateParkingUseCnt(parkingNo, referenceDate, parkingUseCnt);
    }

    @RequestMapping(value="/getOneAdminParking", method=RequestMethod.POST)
    @ResponseBody
    public String getSelectOneAdminParking(String parkingNo, String referenceDate){
        return parkingService.selectOneAdminParking(parkingNo, referenceDate);
    }
}
