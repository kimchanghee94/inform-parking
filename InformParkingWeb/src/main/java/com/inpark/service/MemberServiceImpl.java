package com.inpark.service;

import com.inpark.dao.MemberDao;
import com.inpark.dto.MemberDto;
import com.inpark.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//dao 없이도 바로 호출할 수 있도록 mybatis의 기능을 사용해보았다. 이 기능을 사용하기 위해 bean에 주석처리로 표시해준 부분을 확인
@Service("MemberService")
public class MemberServiceImpl implements MemberService {
    @Autowired
//    private MemberDao memberDao;
    private MemberMapper memberMapper;

    @Override
    public MemberDto selectMember(String id) {
//        return memberDao.selectMember(id);
        return memberMapper.selectMember(id);
    }

    @Override
    public void insertMember(MemberDto mto){
        memberMapper.insertMember(mto);
    }

    @Override
    public int idCheck(String id){
        return memberMapper.idCheck(id);
    }

    @Override
    public int phoneCheck(String phone){ return memberMapper.phoneCheck(phone);}

    @Override
    public MemberDto loginMember(String id){
        return memberMapper.loginMember(id);
    }

    @Override
    public void insertAuth(String id, String auth){
        memberMapper.insertAuth(id, auth);
    }
}
