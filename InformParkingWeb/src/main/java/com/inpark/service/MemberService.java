package com.inpark.service;

import com.inpark.dto.MemberDto;

public interface MemberService {
    MemberDto selectMember(String id);

    void insertMember(MemberDto mto);

    int idCheck(String id);

    int phoneCheck(String phone);

    MemberDto loginMember(String id);

    void insertAuth(String id, String auth);
}