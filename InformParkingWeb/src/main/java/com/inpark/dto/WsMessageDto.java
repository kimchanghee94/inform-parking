package com.inpark.dto;

public class WsMessageDto {
    private String parkingNo;
    private String referenceDate;
    private int parkingUseCnt;

    public int getParkingUseCnt() {
        return parkingUseCnt;
    }

    public void setParkingUseCnt(int parkingUseCnt) {
        this.parkingUseCnt = parkingUseCnt;
    }

    public String getParkingNo() {
        return parkingNo;
    }

    public void setParkingNo(String parkingNo) {
        this.parkingNo = parkingNo;
    }

    public String getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(String referenceDate) {
        this.referenceDate = referenceDate;
    }
}
