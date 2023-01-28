package com.inpark.service;

import com.fasterxml.jackson.core.util.BufferRecycler;
import com.inpark.dto.MemberDto;
import com.inpark.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

//dao 없이도 바로 호출할 수 있도록 mybatis의 기능을 사용해보았다. 이 기능을 사용하기 위해 bean에 주석처리로 표시해준 부분을 확인
@Service("MemberService")
public class MemberServiceImpl implements MemberService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
//    private MemberDao memberDao;
    private MemberMapper memberMapper;

    @Override
    public MemberDto selectMember(String id) {
//        return memberDao.selectMember(id);
        return memberMapper.selectMember(id);
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

    //카카오페이 API를 이용해 주차장 결제를 이용한다.
    @Override
    public String buyParkingWithKakaoPay(){
        try{
            URL kakaoPayURL = new URL("https://kapi.kakao.com/v1/payment/ready");
            HttpURLConnection conn = (HttpURLConnection)kakaoPayURL.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "KakaoAK 77407cf43f48d65697372afc8075ebce");
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setDoOutput(true);

            StringBuilder reqParam = new StringBuilder(URLEncoder.encode("cid","UTF-8") + "=TC0ONETIME");
            reqParam.append("&" + URLEncoder.encode("partner_order_id", "UTF-8") + "=partner_order_id");
            reqParam.append("&" + URLEncoder.encode("partner_user_id", "UTF-8") + "=partner_user_id");
            reqParam.append("&" + URLEncoder.encode("item_name", "UTF-8") + "=parkingName(day or month)");
            reqParam.append("&" + URLEncoder.encode("quantity", "UTF-8") + "=1");
            reqParam.append("&" + URLEncoder.encode("total_amount", "UTF-8") + "=50000"); //주차장 가격
            reqParam.append("&" + URLEncoder.encode("tax_free_amount", "UTF-8") + "=25000"); //상품 비과세 금액
            reqParam.append("&" + URLEncoder.encode("approval_url", "UTF-8") + "=https://www.inparking.online/approveKakaopay");
            reqParam.append("&" + URLEncoder.encode("fail_url", "UTF-8") + "=https://www.inparking.online/home");
            reqParam.append("&" + URLEncoder.encode("cancel_url", "UTF-8") + "=https://www.inparking.online/home");

            OutputStream reqStream = conn.getOutputStream();
            DataOutputStream dataReqStream = new DataOutputStream(reqStream);
            dataReqStream.writeBytes(reqParam.toString());
            dataReqStream.close(); //전기줄에 태워보내면서 flush호출도 하고 그리고 DataOutputStream을 닫는다.

            BufferedReader rd;

            if(conn.getResponseCode() == 200){ //getResponseCode를 통해 결과값을 받아온다.
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }else{
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while((line = rd.readLine()) != null){
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            System.out.println(sb.toString());
            return sb.toString();
        }catch(MalformedURLException me){
            me.printStackTrace();
        }catch(IOException ie){
            ie.printStackTrace();
        }
        return "{\"result\":\"No\"}";
    }

    @Override
    public void approveKakaoPay(String tid, String token){
        try{
            URL kakaoPayURL = new URL("https://kapi.kakao.com/v1/payment/approve");
            HttpURLConnection conn = (HttpURLConnection)kakaoPayURL.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "KakaoAK 77407cf43f48d65697372afc8075ebce");
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setDoOutput(true);

            System.out.println("approveKakaoPay : " + tid + ", " + token);

            StringBuilder reqParam = new StringBuilder(URLEncoder.encode("cid","UTF-8") + "=TC0ONETIME");
            reqParam.append("&" + URLEncoder.encode("tid", "UTF-8") + "=" + tid);
            reqParam.append("&" + URLEncoder.encode("partner_order_id", "UTF-8") + "=partner_order_id");
            reqParam.append("&" + URLEncoder.encode("partner_user_id", "UTF-8") + "=partner_user_id");
            reqParam.append("&" + URLEncoder.encode("pg_token", "UTF-8") + "=" + token);

            OutputStream reqStream = conn.getOutputStream();
            DataOutputStream dataReqStream = new DataOutputStream(reqStream);
            dataReqStream.writeBytes(reqParam.toString());
            dataReqStream.close(); //전기줄에 태워보내면서 flush호출도 하고 그리고 DataOutputStream을 닫는다.

            BufferedReader rd;

            if(conn.getResponseCode() == 200){ //getResponseCode를 통해 결과값을 받아온다.
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }else{
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while((line = rd.readLine()) != null){
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            System.out.println(sb.toString());
        }catch(MalformedURLException me){
            me.printStackTrace();
        }catch(IOException ie){
            ie.printStackTrace();
        }
    }
}
