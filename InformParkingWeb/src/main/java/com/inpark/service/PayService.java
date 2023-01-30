package com.inpark.service;

import org.springframework.stereotype.Service;

public interface PayService {
    String buyParkingWithKakaoPay(String parkingNo, String parkingName, String parkingPrice);

    void approveKakaoPay(String tid, String token, String parkingNo);
}
