package com.inpark.dto;

public class AdminParkingDto {
    String parkingNo;
    String referenceDate;
    String parkingName;
    int parkingUseCnt;
    String users_id;

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

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public int getParkingUseCnt() {
        return parkingUseCnt;
    }

    public void setParkingUseCnt(int parkingUseCnt) {
        this.parkingUseCnt = parkingUseCnt;
    }

    public String getUsers_id() {
        return users_id;
    }

    public void setUsers_id(String users_id) {
        this.users_id = users_id;
    }
}
