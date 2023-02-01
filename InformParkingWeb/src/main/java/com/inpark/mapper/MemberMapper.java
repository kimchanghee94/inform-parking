package com.inpark.mapper;

import com.inpark.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {
    MemberDto selectMemberByJson(String id);

    /*회원가입*/
    void insertMember(MemberDto mto);

    /*id 중복 검사*/
    int selectIdCheck(String id);

    /*phone 중복 검사*/
    int selectPhoneCheck(String phone);

    /*로그인*/
    MemberDto selectLoginMember(String id);

    /*권한 가져오기 */
    List<String> selectUserAuth(String id);

    /*회원가입 시 권한 설정도 넣기*/
    void insertAuth(@Param("id")String id, @Param("auth")String auth);

    void deleteAuthUserRoleAdmin(@Param("id")String id);

    void updateMemberCarNum(@Param("id")String id, @Param("carNum")String carNum);

    List<MemberDto> selectSubCarNumberList(@Param("id")String id);

    void insertSubCarNum(@Param("id")String id, @Param("carNum")String carNum);

    void deleteCarNum(@Param("id")String id, @Param("carNum")String carNum);

    void updateSubCarNum(@Param("id")String id, @Param("selectedCarNum")String selectedCarNum, @Param("repCarNum")String repCarNum);
}
