package com.inpark.service;

import com.inpark.dto.CompParkingDto;
import com.inpark.dto.ParkingDto;
import com.inpark.mapper.ParkingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ParkingService")
public class ParkingServiceImpl implements ParkingService {

    @Autowired
    private ParkingMapper parkingMapper;

    @Override
    public List<ParkingDto> selectViewParking(CompParkingDto compParkingDto){
        return parkingMapper.selectViewParking(compParkingDto);
    }

    @Override
    public void insertParking(List<ParkingDto> dto){
        parkingMapper.insertParking(dto);
    }

    @Override
    public void deleteAllParking(){
        parkingMapper.deleteAllParking();
    }

}
