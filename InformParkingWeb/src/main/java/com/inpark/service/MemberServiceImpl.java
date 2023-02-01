package com.inpark.service;

import com.fasterxml.jackson.core.util.BufferRecycler;
import com.inpark.dto.MemberDto;
import com.inpark.dto.PayDto;
import com.inpark.mapper.MemberMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

//dao 없이도 바로 호출할 수 있도록 mybatis의 기능을 사용해보았다. 이 기능을 사용하기 위해 bean에 주석처리로 표시해준 부분을 확인
@Service("MemberService")
public class MemberServiceImpl implements MemberService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
//    private MemberDao memberDao;
    private MemberMapper memberMapper;

    @Override
    public String selectMemberByJson(String id) {
//        return memberDao.selectMember(id);

        MemberDto memberDto = memberMapper.selectMemberByJson(id);

        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();

        if(memberDto != null){
            header.put("statusCode", "00");
            header.put("msg", "Get User Inform Access Success");
            body.put("id", memberDto.getId());
            body.put("userName", memberDto.getUserName());
            body.put("carNum", memberDto.getCarNum());
            body.put("phone", memberDto.getPhone());
        }else{
            header.put("statusCode", "01");
            header.put("msg", "Get User Inform Access Fail");
        }

        root.put("header", header);
        root.put("body", body);

        return root.toJSONString();
    }

    @Override
    public String updateMemberCarNum(String id, String carNum){
        memberMapper.updateMemberCarNum(id, carNum);

        /*json작업 시작*/
        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();

        header.put("msg", "Success update User Car Number");
        header.put("statusCode", "00");
        root.put("header", header);

        System.out.println(root.toString());

        return root.toJSONString();
    }

    @Override
    public String selectSubCarNumberList(String id){
        List<MemberDto> memberDtoList = memberMapper.selectSubCarNumberList(id);

        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        JSONArray items = new JSONArray();

        if(memberDtoList != null && memberDtoList.size() > 0){
            header.put("statusCode", "00");
            header.put("msg", "get Car Num List Success");

            for(MemberDto tmpMemberDto : memberDtoList){
                JSONObject temp = new JSONObject();
                temp.put("carNum", tmpMemberDto.getCarNum());
                items.add(temp);
            }

            body.put("items", items);
        }else{
            header.put("statusCode", "01");
            header.put("msg", "There's no Car Num List");
        }

        root.put("header", header);
        root.put("body", body);

        System.out.println(root.toString());
        return root.toJSONString();
    }

    @Override
    public String insertCarNum(String id, String carNum){

        //현재 대표차량이 등록되어있는지 확인하고 없을 경우 대표차량부터 등록
        MemberDto memberDto = memberMapper.selectLoginMember(id);

        /*json작업 시작*/
        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();

        //insert subcar table
        if(memberDto.getCarNum() != null && memberDto.getCarNum().length() > 0){
            //user테이블에 중복된 값이 있는지 확인
            if(carNum.equals(memberDto.getCarNum()) == true){
                header.put("msg", "It's already registered");
                header.put("statusCode", "01");
                root.put("header", header);

                System.out.println(root.toString());

                return root.toJSONString();
            }
            //subcar 목록에 중복된 값이 있는지 확인
            List<MemberDto> memberDtoList = memberMapper.selectSubCarNumberList(id);
            for(MemberDto tmpMemberDto : memberDtoList){
                if(carNum.equals(tmpMemberDto.getCarNum()) == true){
                    header.put("msg", "It's already registered");
                    header.put("statusCode", "01");
                    root.put("header", header);

                    System.out.println(root.toString());

                    return root.toJSONString();
                }
            }

            memberMapper.insertSubCarNum(id, carNum);

            header.put("msg", "Success insert user Car Number");
            header.put("statusCode", "00");
        }
        //update user table
        else{
            memberMapper.updateMemberCarNum(id, carNum);

            header.put("msg", "Success update user Car Number");
            header.put("statusCode", "00");
        }

        root.put("header", header);

        System.out.println(root.toString());

        return root.toJSONString();
    }

    @Override
    public String deleteCarNum(String id, String carNum){
        //현재 대표차량이 등록되어있는지 확인하고 없을 경우 대표차량부터 등록
        MemberDto memberDto = memberMapper.selectLoginMember(id);

        /*json작업 시작*/
        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();

        List<MemberDto> memberDtoList = memberMapper.selectSubCarNumberList(id);

        //delete subcar table
        if(memberDtoList != null && memberDtoList.size() > 0){
            memberMapper.deleteCarNum(id, carNum);

            header.put("msg", "Success delete user Car Number");
            header.put("statusCode", "00");
        }
        //update user table
        else if(memberDto.getCarNum() != null && memberDto.getCarNum().length() > 0){
            memberMapper.updateMemberCarNum(id, null);

            header.put("msg", "Success update user Car Number to Null");
            header.put("statusCode", "00");
        }else{
            header.put("msg", "There is no register car");
            header.put("statusCode", "01");
        }

        root.put("header", header);

        System.out.println(root.toString());

        return root.toJSONString();
    }

    @Override
    public void insertMember(MemberDto dto){
        System.out.println("암호화 전 : " + dto.getPasswd());
        dto.setPasswd(bCryptPasswordEncoder.encode(dto.getPasswd()));
        System.out.println("암호화 후 : " + dto.getPasswd());

        memberMapper.insertMember(dto);
        memberMapper.insertAuth(dto.getId(), "ROLE_manager");
    }

    @Override
    public String selectIdCheck(String id){
        System.out.println("id 중복체크 컨트롤러 진입" + id);
        int result = memberMapper.selectIdCheck(id);
        System.out.println("result : " + result);
        if(result != 0){
            return "fail";
        } else {
            return "success";
        }
    }

    @Override
    public String selectPhoneCheck(String phone){
        System.out.println("phone 중복 체크" + phone);
        int result = memberMapper.selectPhoneCheck(phone);
        System.out.println("result : " + result);
        if(result != 0){
            return "fail";
        } else {
            return "success";
        }
    }

    @Override
    public MemberDto selectLoginMember(String id){
        return memberMapper.selectLoginMember(id);
    }

    @Override
    public void insertAuth(String id, String auth){
        memberMapper.insertAuth(id, auth);
    }
}
