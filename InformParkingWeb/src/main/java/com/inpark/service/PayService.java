package com.inpark.service;

import org.springframework.stereotype.Service;

public interface PayService {
    String buyParkingWithKakaoPay();

    void approveKakaoPay(String tid, String token);
}
