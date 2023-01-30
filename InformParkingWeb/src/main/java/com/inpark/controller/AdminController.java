package com.inpark.controller;

import com.inpark.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @RequestMapping(value="/getParkingAuthCheck", method= RequestMethod.POST)
    @ResponseBody
    public String getParkingAuthCheck(String parkingNo, String referenceDate){
        System.out.println("parkingNo : " + parkingNo + "referenceDate : " + referenceDate);
        return adminService.selectAuthParkingAdmin(parkingNo, referenceDate);
    }

    @RequestMapping(value="/addDBAdminRolePage", method=RequestMethod.POST)
    @ResponseBody
    public String addDBAdminRolePage(String parkingNo, String referenceDate) throws Exception{
        return adminService.insertAdminParking(parkingNo, referenceDate);
    }

    @RequestMapping(value="/deleteAdminParkingField", method=RequestMethod.POST)
    @ResponseBody
    public String deleteAdminParkingField(String parkingNo, String referenceDate) throws Exception{
        return adminService.deleteAdminParkingField(parkingNo, referenceDate);
    }

    @RequestMapping(value="/getInitSettingAdmin", method=RequestMethod.POST)
    @ResponseBody
    public String getInitSettingAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String id = userDetails.getUsername();

        return adminService.selectAdminParkingList(id);
    }

    @RequestMapping(value="/updateParkingUseCnt", method=RequestMethod.POST)
    @ResponseBody
    public String insertParkingUseCnt(String parkingNo, String referenceDate, int parkingUseCnt) throws Exception {
        return adminService.updateParkingUseCnt(parkingNo, referenceDate, parkingUseCnt);
    }

    @RequestMapping(value="/getOneAdminParking", method=RequestMethod.POST)
    @ResponseBody
    public String getSelectOneAdminParking(String parkingNo, String referenceDate){
        return adminService.selectOneAdminParking(parkingNo, referenceDate);
    }
}
