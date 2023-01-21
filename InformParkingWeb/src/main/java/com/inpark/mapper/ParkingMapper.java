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

    int selectAuthParkingAdmin(@Param("parkingNo")String parkingNo, @Param("referenceDate")String referenceDate);

    int selectCheckAuthParkingAdmin(@Param("parkingNo")String parkingNo);

    String selectGetParkingName(@Param("parkingNo")String parkingNo, @Param("referenceDate")String referenceDate);

    void insertAdminParking(AdminParkingDto apDto);

    List<AdminParkingDto> selectAdminParkingList(@Param("id")String id);

    void deleteAdminParkingField(@Param("parkingNo")String parkingNo, @Param("referenceDate")String referenceDate);

    void updateParkingUseCnt(@Param("parkingNo")String parkingNo, @Param("referenceDate")String referenceDate, @Param("parkingUseCnt")int parkingUseCnt);
}
