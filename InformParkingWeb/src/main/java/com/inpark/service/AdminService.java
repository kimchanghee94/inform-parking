package com.inpark.service;

public interface AdminService {
    String selectAuthParkingAdmin(String parkingNo, String referenceDate);

    String selectGetParkingName(String parkingNo, String referenceDate);

    String insertAdminParking(String parkingNo, String referenceDate) throws Exception;

    String selectAdminParkingList(String id);

    String deleteAdminParkingField(String parkingNo, String referenceDate) throws Exception;

    String updateParkingUseCnt(String parkingNo, String referenceDate, int parkingUseCnt) throws Exception;

    String selectOneAdminParking(String parkingNo, String referenceDate);
}
