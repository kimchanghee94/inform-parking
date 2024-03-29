package com.inpark.dto;

public class ParkingDto {
    private int id;
    private String parkingName;
    private String phone;
    private double latitude;
    private double longitude;

    private String parkingFor;
    private String parkingStruct;
    private String rdnmadr;
    private String lnmadr;
    private String openDay;
    private String parkingCnt;

    /* 운영시간 */
    private String weekOpen;
    private String weekClose;
    private String satOpen;
    private String satClose;
    private String holiOpen;
    private String holiClose;

    /* 요금정보 */
    private String parkingFreeInfo;
    private String basicTime;
    private String basicCharge;
    private String addUnitTime;
    private String addUnitCharge;
    private String dayChargeTime;
    private String dayCharge;
    private String monthCharge;

    /* 주차장 인증하기 */
    private String parkingNo;
    private String referenceDate;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getParkingFor() {
        return parkingFor;
    }

    public void setParkingFor(String parkingFor) {
        this.parkingFor = parkingFor;
    }

    public String getParkingStruct() {
        return parkingStruct;
    }

    public void setParkingStruct(String parkingStruct) {
        this.parkingStruct = parkingStruct;
    }

    public String getRdnmadr() {
        return rdnmadr;
    }

    public void setRdnmadr(String rdnmadr) {
        this.rdnmadr = rdnmadr;
    }

    public String getLnmadr() {
        return lnmadr;
    }

    public void setLnmadr(String lnmadr) {
        this.lnmadr = lnmadr;
    }

    public String getOpenDay() {
        return openDay;
    }

    public void setOpenDay(String openDay) {
        this.openDay = openDay;
    }

    public String getParkingCnt() {
        return parkingCnt;
    }

    public void setParkingCnt(String parkingCnt) {
        this.parkingCnt = parkingCnt;
    }

    public String getWeekOpen() {
        return weekOpen;
    }

    public void setWeekOpen(String weekOpen) {
        this.weekOpen = weekOpen;
    }

    public String getWeekClose() {
        return weekClose;
    }

    public void setWeekClose(String weekClose) {
        this.weekClose = weekClose;
    }

    public String getSatOpen() {
        return satOpen;
    }

    public void setSatOpen(String satOpen) {
        this.satOpen = satOpen;
    }

    public String getSatClose() {
        return satClose;
    }

    public void setSatClose(String satClose) {
        this.satClose = satClose;
    }

    public String getHoliOpen() {
        return holiOpen;
    }

    public void setHoliOpen(String holiOpen) {
        this.holiOpen = holiOpen;
    }

    public String getHoliClose() {
        return holiClose;
    }

    public void setHoliClose(String holiClose) {
        this.holiClose = holiClose;
    }

    public String getParkingFreeInfo() {
        return parkingFreeInfo;
    }

    public void setParkingFreeInfo(String parkingFreeInfo) {
        this.parkingFreeInfo = parkingFreeInfo;
    }

    public String getBasicTime() {
        return basicTime;
    }

    public void setBasicTime(String basicTime) {
        this.basicTime = basicTime;
    }

    public String getBasicCharge() {
        return basicCharge;
    }

    public void setBasicCharge(String basicCharge) {
        this.basicCharge = basicCharge;
    }

    public String getAddUnitTime() {
        return addUnitTime;
    }

    public void setAddUnitTime(String addUnitTime) {
        this.addUnitTime = addUnitTime;
    }

    public String getAddUnitCharge() {
        return addUnitCharge;
    }

    public void setAddUnitCharge(String addUnitCharge) {
        this.addUnitCharge = addUnitCharge;
    }

    public String getDayCharge() {
        return dayCharge;
    }

    public void setDayCharge(String dayCharge) {
        this.dayCharge = dayCharge;
    }

    public String getMonthCharge() {
        return monthCharge;
    }

    public void setMonthCharge(String monthCharge) {
        this.monthCharge = monthCharge;
    }

    public String getDayChargeTime() {
        return dayChargeTime;
    }

    public void setDayChargeTime(String dayChargeTime) {
        this.dayChargeTime = dayChargeTime;
    }

    @Override
    public String toString() {
        return "ParkingDto{" +
                "id=" + id +
                ", parkingName='" + parkingName + '\'' +
                ", phone='" + phone + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", parkingFor='" + parkingFor + '\'' +
                ", parkingStruct='" + parkingStruct + '\'' +
                ", rdnmadr='" + rdnmadr + '\'' +
                ", lnmadr='" + lnmadr + '\'' +
                ", openDay='" + openDay + '\'' +
                ", parkingCnt='" + parkingCnt + '\'' +
                ", weekOpen='" + weekOpen + '\'' +
                ", weekClose='" + weekClose + '\'' +
                ", satOpen='" + satOpen + '\'' +
                ", satClose='" + satClose + '\'' +
                ", holiOpen='" + holiOpen + '\'' +
                ", holiClose='" + holiClose + '\'' +
                ", parkingFreeInfo='" + parkingFreeInfo + '\'' +
                ", basicTime='" + basicTime + '\'' +
                ", basicCharge='" + basicCharge + '\'' +
                ", addUnitTime='" + addUnitTime + '\'' +
                ", addUnitCharge='" + addUnitCharge + '\'' +
                ", dayChargeTime='" + dayChargeTime + '\'' +
                ", dayCharge='" + dayCharge + '\'' +
                ", monthCharge='" + monthCharge + '\'' +
                '}';
    }
}
