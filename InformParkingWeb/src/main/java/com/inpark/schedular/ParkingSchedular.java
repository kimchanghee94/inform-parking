package com.inpark.schedular;

import com.inpark.dto.ParkingDto;
import com.inpark.service.ParkingService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ParkingSchedular {

    @Autowired
    private ParkingService parkingService;
    //임시테이블을 하나 더 뚫는다 => 거기에 채워 넣고 parking 테이블의 데이터랑 비교를 하고 바뀌었을 경우 넘긴다.
    //transaction 처리로 문제가 발생할 경우 롤백을 할 수 있다.
    //@Scheduled(cron="0 0 01 * * ?")
    public void getLatLng() throws IOException {
        List<ParkingDto> parkingList = new ArrayList<>();
        System.out.println("새벽 1시가 되면 데이터를 한번 비우고 위도와 경도를 DB에 새로 구축한다.");

        parkingService.deleteAllParking();

        int i=0;

        //현재 147페이지까지 지원
        while(true && i<148){
            String pageStr = String.valueOf(i++);
            StringBuilder urlBuilder = new StringBuilder("http://api.data.go.kr/openapi/tn_pubr_prkplce_info_api"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=NTcVlKsvXzUj48Saf4Jm63ab%2BPhUSusJytZOsOwuFqm1pO8XJhJRGIUEmKQdMbcoD%2FUcEHyFCcSQ4mHr1Fs2gA%3D%3D"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(pageStr, "UTF-8")); /*페이지 번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("100", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*XML/JSON 여부*/

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            //System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                break;
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            try{
                JSONParser parser = new JSONParser();
                JSONObject object = (JSONObject) parser.parse(sb.toString());
                JSONObject response = (JSONObject) object.get("response");
                JSONObject body = (JSONObject) response.get("body");
                JSONArray items = (JSONArray) body.get("items");

                for(int j=0; j<items.size(); j++){
                    object = (JSONObject) items.get(j);

                    if(((String)object.get("latitude")).length() != 0
                            && ((String)object.get("longitude")).length() != 0){
                        ParkingDto parkingDto = new ParkingDto();
                        parkingDto.setId(parkingList.size());
                        parkingDto.setParkingName((String)object.get("prkplceNm"));
                        parkingDto.setLatitude(Double.parseDouble((String)object.get("latitude")));
                        parkingDto.setLongitude(Double.parseDouble((String)object.get("longitude")));
                        parkingDto.setPhone((String)object.get("phoneNumber"));

                        parkingDto.setParkingFor((String)object.get("prkplceSe"));
                        parkingDto.setParkingStruct((String)object.get("prkplceType"));
                        parkingDto.setRdnmadr((String)object.get("rdnmadr"));
                        parkingDto.setLnmadr((String)object.get("lnmadr"));
                        parkingDto.setOpenDay((String)object.get("operDay"));
                        parkingDto.setParkingCnt((String)object.get("prkcmprt"));

                        parkingDto.setWeekOpen((String)object.get("weekdayOperOpenHhmm"));
                        parkingDto.setWeekClose((String)object.get("weekdayOperColseHhmm"));
                        parkingDto.setSatOpen((String)object.get("satOperOperOpenHhmm"));
                        parkingDto.setSatClose((String)object.get("satOperCloseHhmm"));
                        parkingDto.setHoliOpen((String)object.get("holidayOperOpenHhmm"));
                        parkingDto.setHoliClose((String)object.get("holidayCloseOpenHhmm"));

                        parkingDto.setParkingFreeInfo((String)object.get("parkingchrgeInfo"));
                        parkingDto.setBasicTime((String)object.get("basicTime"));
                        parkingDto.setBasicCharge((String)object.get("basicCharge"));
                        parkingDto.setAddUnitTime((String)object.get("addUnitTime"));
                        parkingDto.setAddUnitCharge((String)object.get("addUnitCharge"));
                        parkingDto.setDayChargeTime((String)object.get("dayCmmtktAdjTime"));
                        parkingDto.setDayCharge((String)object.get("dayCmmtkt"));
                        parkingDto.setMonthCharge((String)object.get("monthCmmtkt"));

                        parkingList.add(parkingDto);
                    }
                }
            }catch(ParseException pe){
                pe.printStackTrace();
            }
        }
        for(ParkingDto dto : parkingList){
            System.out.println(dto);
        }
        parkingService.insertParking(parkingList);
    }
}
