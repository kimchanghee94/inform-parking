package com.inpark.mapper;

import com.inpark.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    MemberDto selectMember(String id);
}
