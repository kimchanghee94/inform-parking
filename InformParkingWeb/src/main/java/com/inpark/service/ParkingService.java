package com.inpark.service;

import com.inpark.dto.ParkingDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ParkingService {
    String combSelectParking(String neLat, String neLng, String swLat, String swLng);

    void insertParking(List<ParkingDto> dto) throws Exception;

    void deleteAllParking();

    void insertRefreshParkingInfo() throws Exception;

    int selectCountParkingRow();

    String selectAuthParkingAdmin(String parkingNo, String referenceDate);

    String selectGetParkingName(String parkingNo, String referenceDate);

    String insertAdminParking(String parkingNo, String referenceDate) throws Exception;

    String selectAdminParkingList(String id);

    String deleteAdminParkingField(String parkingNo, String referenceDate) throws Exception;

    String updateParkingUseCnt(String parkingNo, String referenceDate, int parkingUseCnt) throws Exception;
}
