package com.inpark.service;

import com.inpark.dto.CompParkingDto;
import com.inpark.dto.ParkingDto;

import java.io.IOException;
import java.util.List;

public interface ParkingService {
    String combSelectParking(String neLat, String neLng, String swLat, String swLng);

    void insertParking(List<ParkingDto> dto);

    void deleteAllParking();

    void refreshParkingInfo() throws IOException;
}
