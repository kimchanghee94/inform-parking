package com.inpark.service;

import com.inpark.dto.PayDto;
import com.inpark.mapper.PayMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

@Service("PayService")
public class PayServiceImpl implements PayService{
    @Autowired
    PayMapper payMapper;

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
            reqParam.append("&" + URLEncoder.encode("partner_order_id", "UTF-8") + "=" + URLEncoder.encode(parkingNo, "UTF-8"));
            reqParam.append("&" + URLEncoder.encode("partner_user_id", "UTF-8") + "=" + URLEncoder.encode("주차장을 알리다", "UTF-8"));
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
    public void approveKakaoPay(String tid, String token, String parkingNo, PayDto payDto){
        int approveSuccessFlag = 0;

        try{
            URL kakaoPayURL = new URL("https://kapi.kakao.com/v1/payment/approve");
            HttpURLConnection conn = (HttpURLConnection)kakaoPayURL.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "KakaoAK 77407cf43f48d65697372afc8075ebce");
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setDoOutput(true);

            StringBuilder reqParam = new StringBuilder(URLEncoder.encode("cid","UTF-8") + "=TC0ONETIME");
            reqParam.append("&" + URLEncoder.encode("tid", "UTF-8") + "=" + tid);
            reqParam.append("&" + URLEncoder.encode("partner_order_id", "UTF-8") + "=" + URLEncoder.encode(parkingNo, "UTF-8"));
            reqParam.append("&" + URLEncoder.encode("partner_user_id", "UTF-8") + "=" + URLEncoder.encode("주차장을 알리다", "UTF-8"));
            reqParam.append("&" + URLEncoder.encode("pg_token", "UTF-8") + "=" + token);

            OutputStream reqStream = conn.getOutputStream();
            DataOutputStream dataReqStream = new DataOutputStream(reqStream);
            dataReqStream.writeBytes(reqParam.toString());
            dataReqStream.close(); //전기줄에 태워보내면서 flush호출도 하고 그리고 DataOutputStream을 닫는다.

            BufferedReader rd;

            if(conn.getResponseCode() == 200){ //getResponseCode를 통해 결과값을 받아온다.
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                approveSuccessFlag = 1;
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

            /* 성공적으로 결제 승인이 되어 날짜 갱신 후 history에 입력한다. */
            if(approveSuccessFlag == 1){
                try{
                    JSONParser parser = new JSONParser();
                    JSONObject object = (JSONObject) parser.parse(sb.toString());

                    String approvedTime = (String)object.get("approved_at");
                    approvedTime = approvedTime.replace("T", " ");

                    payDto.setPurchaseTime(approvedTime);
                    insertPurchaseHistory(payDto);
                }catch(ParseException pe){
                    pe.printStackTrace();
                }
            }

            System.out.println(sb.toString());
        }catch(MalformedURLException me){
            me.printStackTrace();
        }catch(IOException ie){
            ie.printStackTrace();
        }
    }

    @Override
    public void insertPurchaseHistory(PayDto payDto){
        payMapper.insertPurchaseHistory(payDto);
    }

    @Override
    public String selectPurchaseHistory(String id){
        List<PayDto> payDtoList = payMapper.selectPurchaseHistory(id);

        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        JSONArray items = new JSONArray();

        if(payDtoList != null && payDtoList.size() > 0){
            header.put("statusCode", "00");
            header.put("msg", "get Purchase History Success");

            for(PayDto tmpPayDto : payDtoList){
                JSONObject temp = new JSONObject();
                temp.put("parkingName", tmpPayDto.getParkingName());
                temp.put("purchaseTime", tmpPayDto.getPurchaseTime());
                temp.put("parkingPrice", tmpPayDto.getParkingPrice());
                temp.put("carNum", tmpPayDto.getCarNum());
                temp.put("dayMonth", tmpPayDto.getDayMonth());

                items.add(temp);
            }

            body.put("items", items);
        }else{
            header.put("statusCode", "01");
            header.put("msg", "There's no Purchase History Data");
        }

        root.put("header", header);
        root.put("body", body);

        System.out.println(root.toString());
        return root.toJSONString();
    }
}
