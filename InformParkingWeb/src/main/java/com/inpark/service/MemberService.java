package com.inpark.service;

import com.inpark.dto.MemberDto;

public interface MemberService {
    String selectMemberByJson(String id);

    void insertMember(MemberDto dto);

    String selectIdCheck(String id);

    String selectPhoneCheck(String phone);

    MemberDto selectLoginMember(String id);

    void insertAuth(String id, String auth);

    String updateMemberCarNum(String id, String carNum);

    String selectSubCarNumberList(String id);

    String insertCarNum(String id, String carNum);

    String deleteCarNum(String id, String carNum);

    String updateSwitchRepUserCarNum(String id, String repCarNum, String selectedCarNum);
}