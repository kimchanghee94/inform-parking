package com.inpark.mapper;

import com.inpark.dto.CompParkingDto;
import com.inpark.dto.ParkingDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ParkingMapper {
    List<ParkingDto> selectViewParking(CompParkingDto compParkingDto);

    void insertParking(List<ParkingDto> dto);

    void deleteAllParking();

    int countParkingRow();
}
