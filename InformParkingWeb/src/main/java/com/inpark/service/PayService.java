package com.inpark.service;

import com.inpark.dto.PayDto;

public interface PayService {
    String buyParkingWithKakaoPay(String parkingNo, String parkingName, String parkingPrice);

    void approveKakaoPay(String tid, String token, String parkingNo, PayDto payDto);

    void insertPurchaseHistory(PayDto payDto);

    String selectPurchaseHistory(String id);
}
