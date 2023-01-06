package com.inpark.service;

import com.inpark.dto.CompParkingDto;
import com.inpark.dto.ParkingDto;

import java.util.List;

public interface ParkingService {
    List<ParkingDto> selectViewParking(CompParkingDto compParkingDto);

    void insertParking(List<ParkingDto> dto);

    void deleteAllParking();
}
