package com.inpark.service;

import com.inpark.dto.MemberDto;

public interface MemberService {
    MemberDto selectMember(String id);

    void insertMember(MemberDto dto);

    String idCheck(String id);

    String phoneCheck(String phone);

    MemberDto loginMember(String id);

    void insertAuth(String id, String auth);
}