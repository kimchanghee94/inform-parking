package com.inpark.service;

import com.inpark.dto.AdminParkingDto;
import com.inpark.dto.ParkingDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ParkingService {
    String combSelectParking(String neLat, String neLng, String swLat, String swLng);

    void insertParking(List<ParkingDto> dto) throws Exception;

    void deleteAllParking();

    void insertRefreshParkingInfo() throws Exception;

    int selectCountParkingRow();
}
