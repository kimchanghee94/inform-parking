package com.inpark.dao;

import com.inpark.dto.MemberDto;
import com.inpark.mapper.MemberMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {
    @Autowired
    private SqlSession sqlSession;

    public MemberDto selectMember(final String id){
        MemberMapper mapper = sqlSession.getMapper(MemberMapper.class);

        MemberDto memberDto = mapper.selectLoginMember(id);

        return memberDto;
    }
}
