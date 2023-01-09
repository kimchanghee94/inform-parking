package com.inpark.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;


public class MemberDto {
    private String id;
    private String userName;
    private String passwd;
    private String carNum;
    private String phone;

    /* json형태로 db를 구축할 경우에 사용한다.
    private String info;

    public String getInfo(){
        //json형태로 조합한다.
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("userName", this.userName);
        jsonObj.put("passwd", this.passwd);
        jsonObj.put("carNum", this.carNum);
        jsonObj.put("phone", this.phone);
        System.out.println(jsonObj.toString());
        return jsonObj.toString();
    }

    public void setInfo(String info){
        //json데이터를 분해하여 각 변수에 조합한다.
        this.info = info;
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            MemberDto member = objectMapper.readValue(info, MemberDto.class);
            this.userName = member.getUserName();
            this.passwd = member.getPasswd();
            this.carNum = member.getCarNum();
            this.phone = member.getPhone();
        } catch (JsonMappingException jme){
            jme.printStackTrace();
        } catch(JsonProcessingException jpe){
            jpe.printStackTrace();
        }
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String toString(){
        return id + ", " + userName + ", " + passwd + ", " + phone + ", " + carNum;
    }
}
