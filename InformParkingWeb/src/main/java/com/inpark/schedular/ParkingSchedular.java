package com.inpark.schedular;

import com.inpark.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;

public class ParkingSchedular {

    @Autowired
    private ParkingService parkingService;
    //임시테이블을 하나 더 뚫는다 => 거기에 채워 넣고 parking 테이블의 데이터랑 비교를 하고 바뀌었을 경우 넘긴다.
    //transaction 처리로 문제가 발생할 경우 롤백을 할 수 있다.
    //@Scheduled(cron="0 0 01 * * ?")
    public void getLatLng() throws IOException {
        parkingService.refreshParkingInfo();
    }
}