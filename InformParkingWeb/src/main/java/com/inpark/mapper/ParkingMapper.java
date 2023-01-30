package com.inpark.mapper;

import com.inpark.dto.AdminParkingDto;
import com.inpark.dto.CompParkingDto;
import com.inpark.dto.ParkingDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ParkingMapper {
    List<ParkingDto> selectViewParking(CompParkingDto compParkingDto);

    void insertParking(List<ParkingDto> dto);

    void deleteAllParking();

    int selectCountParkingRow();
}
