package com.inpark.mapper;

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

    int selectAuthParkingAdmin(@Param("parkingNo")String parkingNo, @Param("referenceDate")String referenceDate);

    int selectCheckAuthParkingAdmin(@Param("parkingNo")String parkingNo);

    String selectGetParkingName(@Param("parkingNo")String parkingNo, @Param("referenceDate")String referenceDate);

    void insertAdminParking(@Param("id")String id, @Param("parkingNo")String parkingNo, @Param("parkingCnt")int parkingCnt);
}
