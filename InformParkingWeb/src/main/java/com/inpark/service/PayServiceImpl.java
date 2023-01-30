package com.inpark.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

@Service("PayService")
public class PayServiceImpl implements PayService{
    //카카오페이 API를 이용해 주차장 결제를 이용한다.
    @Override
    public String buyParkingWithKakaoPay(String parkingNo, String parkingName, String parkingPrice){
        try{
            URL kakaoPayURL = new URL("https://kapi.kakao.com/v1/payment/ready");
            HttpURLConnection conn = (HttpURLConnection)kakaoPayURL.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "KakaoAK 77407cf43f48d65697372afc8075ebce");
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setDoOutput(true);

            StringBuilder reqParam = new StringBuilder(URLEncoder.encode("cid","UTF-8") + "=TC0ONETIME");
            reqParam.append("&" + URLEncoder.encode("partner_order_id", "UTF-8") + "=" + parkingNo);
            reqParam.append("&" + URLEncoder.encode("partner_user_id", "UTF-8") + "=주차장을 알리다");
            reqParam.append("&" + URLEncoder.encode("item_name", "UTF-8") + "=" + URLEncoder.encode(parkingName, "UTF-8"));
            reqParam.append("&" + URLEncoder.encode("quantity", "UTF-8") + "=1");
            reqParam.append("&" + URLEncoder.encode("total_amount", "UTF-8") + "=" + parkingPrice); //주차장 가격
            reqParam.append("&" + URLEncoder.encode("tax_free_amount", "UTF-8") + "=0"); //상품 비과세 금액
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
    public void approveKakaoPay(String tid, String token, String parkingNo){
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
            reqParam.append("&" + URLEncoder.encode("partner_order_id", "UTF-8") + "=" + parkingNo);
            reqParam.append("&" + URLEncoder.encode("partner_user_id", "UTF-8") + "=주차장을 알리다");
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
