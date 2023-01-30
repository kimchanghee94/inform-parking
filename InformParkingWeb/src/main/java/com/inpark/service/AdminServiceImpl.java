package com.inpark.service;

import com.inpark.dto.AdminParkingDto;
import com.inpark.mapper.AdminMapper;
import com.inpark.mapper.MemberMapper;
import com.inpark.mapper.ParkingMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("AdminService")
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private MemberMapper memberMapper;

    /* 마커로 표시할 때 현재 주차장 사용중인 자리가 얼마나 되는지 뽑아온다 */
    public String selectOneAdminParking(String parkingNo, String referenceDate){
        AdminParkingDto result = adminMapper.selectOneAdminParking(parkingNo, referenceDate);
        System.out.println(parkingNo + "," + referenceDate);
        /*json작업 시작*/
        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();

        if(result == null){
            header.put("statusCode", "01");
            header.put("msg", "Not use or Admin not manage this parking");
        }else {
            header.put("statusCode", "00");
            header.put("msg", "Success Get Parking Use Count");

            body.put("parkingUseCnt", result.getParkingUseCnt());
        }

        root.put("header", header);
        root.put("body", body);

        System.out.println(root.toString());

        return root.toJSONString();
    }

    @Override
    public String selectGetParkingName(String parkingNo, String referenceDate){
        String result = adminMapper.selectGetParkingName(parkingNo, referenceDate);

        /*json작업 시작*/
        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();

        body.put("parkingName", result);

        if(result == null || result.length() == 0){
            header.put("statusCode", "01");
            header.put("msg", "No Parking Name");
        }else {
            header.put("statusCode", "00");
            header.put("msg", "Success Get Parking Name");
        }


        root.put("header", header);
        root.put("body", body);

        System.out.println(root.toString());

        return root.toJSONString();
    }

    /* parking no, referenceDate가 고유한지 식별한다. */
    @Override
    public String selectAuthParkingAdmin(String parkingNo, String referenceDate){
        int result = adminMapper.selectAuthParkingAdmin(parkingNo, referenceDate);

        /* 이미 등록된 주차장인지 확인한다. */
        int checkDupResult = adminMapper.selectCheckAuthParkingAdmin(parkingNo);

        /*json작업 시작*/
        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();

        body.put("authParkingCnt", result);

        if(checkDupResult == 0){
            if(result == 0){
                header.put("statusCode", "01");
                header.put("msg", "No Parking List");
            }else if(result == 1){
                header.put("statusCode", "00");
                header.put("msg", "Success Auth Parking");
            }else{
                header.put("statusCode", "02");
                header.put("msg", "There duplicate parking auth");
            }
        }else{
            header.put("statusCode", "03");
            header.put("msg", "Already register");
        }

        root.put("header", header);
        root.put("body", body);

        System.out.println(root.toString());

        return root.toJSONString();
    }

    @Override
    //AOP로 Transaction처리
    public String deleteAdminParkingField(String parkingNo, String referenceDate) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String id = userDetails.getUsername();

        adminMapper.deleteAdminParkingField(parkingNo, referenceDate);

        List<AdminParkingDto> listApd = adminMapper.selectAdminParkingList(id);

        /*json작업 시작*/
        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();

        /* 등록된 주차장이 없으면 관리자 권한을 삭제해준다. */
        if (listApd.size() == 0) {

            memberMapper.deleteAuthUserRoleAdmin(id);

            /* security 로그인 로그아웃 없이 바로 정보에 재입력 */
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
            updatedAuthorities.remove(new SimpleGrantedAuthority("ROLE_admin"));
            //add your role here [e.g., new SimpleGrantedAuthority("ROLE_NEW_ROLE")]

            Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), null, updatedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(newAuth);

            header.put("msg", "Spring-Security remove Admin Role");
            header.put("statusCode", "00");
        } else {
            header.put("msg", "Spring-Security doesn't remove Admin Role");
            header.put("statusCode", "01");
        }
        root.put("header", header);

        System.out.println(root.toString());

        return root.toJSONString();
    }

    @Override
    public String updateParkingUseCnt(String parkingNo, String referenceDate, int parkingUseCnt) throws Exception{
        adminMapper.updateParkingUseCnt(parkingNo, referenceDate, parkingUseCnt);

        /*json작업 시작*/
        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();

        header.put("msg", "Success update Parking Use Cnt");
        header.put("statusCode", "00");
        root.put("header", header);

        System.out.println(root.toString());

        return root.toJSONString();
    }

    @Override
    public String insertAdminParking(String parkingNo, String referenceDate) throws Exception{
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String id = userDetails.getUsername();

        List<String> userAuthList = memberMapper.selectUserAuth(id);

        /* 관리자 역할 추가해주기 */
        boolean isContainsAdmin = userAuthList.contains("ROLE_admin");
        if(isContainsAdmin == false){
            memberMapper.insertAuth(id, "ROLE_admin");
            /* security 로그인 로그아웃 없이 바로 정보에 재입력 */
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());

            updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_admin"));
            //add your role here [e.g., new SimpleGrantedAuthority("ROLE_NEW_ROLE")]

            Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), null, updatedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }

        /* 관리자 정보 dto 조합하기 */
        String parkingName = adminMapper.selectGetParkingName(parkingNo, referenceDate);

        AdminParkingDto adminParkingDto = new AdminParkingDto();
        adminParkingDto.setParkingNo(parkingNo);
        adminParkingDto.setParkingName(parkingName);
        adminParkingDto.setUsers_id(id);
        adminParkingDto.setParkingUseCnt(0);
        adminParkingDto.setReferenceDate(referenceDate);

        adminMapper.insertAdminParking(adminParkingDto);

        /*json작업 시작*/
        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();

        body.put("parkingNo", parkingNo);
        body.put("parkingName", parkingName);
        body.put("referenceDate", referenceDate);

        if(isContainsAdmin == false){
            header.put("statusCode", "001");
            header.put("msg", "Success Insert Admin Parking and User Role add Admin");
        }else{
            header.put("statusCode", "000");
            header.put("msg", "Success Insert Admin Parking and User is already Admin");
        }

        root.put("header", header);
        root.put("body", body);

        System.out.println(root.toString());

        return root.toJSONString();
    }

    @Override
    public String selectAdminParkingList(String id){

        List<AdminParkingDto> listApd = adminMapper.selectAdminParkingList(id);

        /*json작업 시작*/
        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        JSONArray items = new JSONArray();

        if(listApd.size() > 0){
            for(AdminParkingDto apd : listApd){
                JSONObject temp = new JSONObject();
                temp.put("parkingName", apd.getParkingName());
                temp.put("parkingNo", apd.getParkingNo());
                temp.put("parkingUseCnt", apd.getParkingUseCnt());
                temp.put("referenceDate", apd.getReferenceDate());

                items.add(temp);
            }

            body.put("items", items);
            body.put("id", id);

            header.put("statusCode", "00");
            header.put("msg", id + " has a Admin Parking Data");
        }else{
            header.put("statusCode", "01");
            header.put("msg", id + " hasn't Admin Parking Data");
        }

        root.put("header", header);
        root.put("body", body);

        System.out.println(root.toString());

        return root.toJSONString();
    }
}
