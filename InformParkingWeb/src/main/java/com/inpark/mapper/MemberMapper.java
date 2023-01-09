package com.inpark.mapper;

import com.inpark.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /*권한 가져오기 */
    List<String> selectUserAuthOne(String id);

    /*회원가입 시 권한 설정도 넣기*/
    void insertAuth(@Param("id")String id, @Param("auth")String auth);
}
