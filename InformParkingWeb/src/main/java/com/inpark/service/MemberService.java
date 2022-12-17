package com.inpark.service;

import com.inpark.dto.MemberDto;

public interface MemberService {
    MemberDto selectMember(String id);
}