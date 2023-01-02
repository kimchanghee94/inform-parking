package com.inpark.mapper;

import com.inpark.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    MemberDto selectMember(String id);

    /*회원가입*/
    void insertMember(MemberDto mto);

    /*id 중복 검사*/
    int idCheck(String id);

    /*phone 중복 검사*/
    int phoneCheck(String phone);

    /*로그인*/
    MemberDto loginMember(String id);
}
