package com.inpark.service;

import com.inpark.dto.MemberDto;
import com.inpark.mapper.MemberMapper;
import com.inpark.dto.UserDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceCustom implements UserDetailsService {
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String inputUserId) {
        System.out.println("해당 security 검문을 하나??" + inputUserId);
        // 최종적으로 리턴해야할 객체
        UserDetailsDto userDetails = new UserDetailsDto();

        // 사용자 정보 select
        MemberDto userInfo = memberMapper.selectLoginMember(inputUserId);
        System.out.println(userInfo);
        // 사용자 정보 없으면 null 처리
        if (userInfo == null) {
            return null;

            // 사용자 정보 있을 경우 로직 전개 (userDetails에 데이터 넣기)
        } else {
            userDetails.setUsername(userInfo.getId());
            userDetails.setPassword(userInfo.getPasswd());
            userDetails.setUsername2(userInfo.getUserName());
            userDetails.setPhone(userInfo.getPhone());
            userDetails.setCarNum(userInfo.getCarNum());
            // 사용자 권한 select해서 받아온 List<String> 객체 주입
            List<String> userAuthList = memberMapper.selectUserAuth(inputUserId);
            System.out.println(userAuthList.toString());
            userDetails.setAuthorities(userAuthList);
        }

        return userDetails;
    }
}
