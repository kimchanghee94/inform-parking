package com.inpark.mapper;

import com.inpark.dto.AdminParkingDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminMapper {
    int selectAuthParkingAdmin(@Param("parkingNo")String parkingNo, @Param("referenceDate")String referenceDate);

    int selectCheckAuthParkingAdmin(@Param("parkingNo")String parkingNo);

    String selectGetParkingName(@Param("parkingNo")String parkingNo, @Param("referenceDate")String referenceDate);

    void insertAdminParking(AdminParkingDto apDto);

    List<AdminParkingDto> selectAdminParkingList(@Param("id")String id);

    AdminParkingDto selectOneAdminParking(@Param("parkingNo")String parkingNo, @Param("referenceDate")String referenceDate);

    void deleteAdminParkingField(@Param("parkingNo")String parkingNo, @Param("referenceDate")String referenceDate);

    void updateParkingUseCnt(@Param("parkingNo")String parkingNo, @Param("referenceDate")String referenceDate, @Param("parkingUseCnt")int parkingUseCnt);

}
