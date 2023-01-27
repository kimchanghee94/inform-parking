package com.inpark.service;

import com.inpark.dto.MemberDto;

public interface MemberService {
    MemberDto selectMember(String id);

    void insertMember(MemberDto dto);

    String selectIdCheck(String id);

    String selectPhoneCheck(String phone);

    MemberDto selectLoginMember(String id);

    void insertAuth(String id, String auth);

    String buyParkingWithKakaoPay();
}