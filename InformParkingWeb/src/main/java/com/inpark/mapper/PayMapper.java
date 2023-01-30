package com.inpark.mapper;

import com.inpark.dto.PayDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PayMapper {
    void insertPurchaseHistory(PayDto payDto);

    List<PayDto> selectPurchaseHistory(@Param("id") String id);
}
