package com.inpark.service;

import com.inpark.dto.AdminParkingDto;
import com.inpark.dto.CompParkingDto;
import com.inpark.dto.ParkingDto;
import com.inpark.mapper.MemberMapper;
import com.inpark.mapper.ParkingMapper;
import org.apache.ibatis.annotations.Param;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("ParkingService")
public class ParkingServiceImpl implements ParkingService {

    @Autowired
    private ParkingMapper parkingMapper;

    @Override
    public String combSelectParking(String neLat, String neLng, String swLat, String swLng){
        Double dneLat = Double.parseDouble(neLat);
        Double dneLng = Double.parseDouble(neLng);
        Double dswLat = Double.parseDouble(swLat);
        Double dswLng = Double.parseDouble(swLng);

        CompParkingDto compParkingDto = new CompParkingDto();
        compParkingDto.setNeLat(dneLat);
        compParkingDto.setNeLng(dneLng);
        compParkingDto.setSwLat(dswLat);
        compParkingDto.setSwLng(dswLng);

        List<ParkingDto> listParking = parkingMapper.selectViewParking(compParkingDto);

        /*json작업 시작*/
        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        JSONArray items = new JSONArray();

        for(int i=0; i<listParking.size(); i++){
            JSONObject temp = new JSONObject();
            temp.put("parkingName", listParking.get(i).getParkingName());
            temp.put("latitude", listParking.get(i).getLatitude());
            temp.put("longitude", listParking.get(i).getLongitude());
            temp.put("phone", listParking.get(i).getPhone());

            temp.put("parkingFor", listParking.get(i).getParkingFor());
            temp.put("parkingStruct", listParking.get(i).getParkingStruct());
            temp.put("rdnmadr", listParking.get(i).getRdnmadr());
            temp.put("lnmadr", listParking.get(i).getLnmadr());
            temp.put("openDay", listParking.get(i).getOpenDay());
            temp.put("parkingCnt", listParking.get(i).getParkingCnt());

            temp.put("weekOpen", listParking.get(i).getWeekOpen());
            temp.put("weekClose", listParking.get(i).getWeekClose());
            temp.put("satOpen", listParking.get(i).getSatOpen());
            temp.put("satClose", listParking.get(i).getSatClose());
            temp.put("holiOpen", listParking.get(i).getHoliOpen());
            temp.put("holiClose", listParking.get(i).getHoliClose());

            temp.put("parkingFreeInfo", listParking.get(i).getParkingFreeInfo());
            temp.put("basicTime", listParking.get(i).getBasicTime());
            temp.put("basicCharge", listParking.get(i).getBasicCharge());
            temp.put("addUnitTime", listParking.get(i).getAddUnitTime());
            temp.put("addUnitCharge", listParking.get(i).getAddUnitCharge());
            temp.put("dayChargeTime", listParking.get(i).getDayChargeTime());
            temp.put("dayCharge", listParking.get(i).getDayCharge());
            temp.put("monthCharge", listParking.get(i).getMonthCharge());

            temp.put("parkingNo", listParking.get(i).getParkingNo());
            temp.put("referenceDate", listParking.get(i).getReferenceDate());

            items.add(temp);
        }

        body.put("items", items);
        root.put("body", body);

        if(listParking.size() == 0){
            header.put("statusCode", "01");
            System.out.println("지도의 마커 값을 받은게 존재하지 않는다.");
        }else{
            header.put("statusCode", "00");
            System.out.println("정상적으로 마커 정보 값을 받아옴");
        }
        root.put("header", header);

        //System.out.println(root.toString());

        return root.toJSONString();
    }

    @Override
    public void insertParking(List<ParkingDto> dto) throws Exception{
        parkingMapper.insertParking(dto);
    }

    @Override
    public void deleteAllParking(){
        parkingMapper.deleteAllParking();
    }

    @Override
    public int selectCountParkingRow(){
        return parkingMapper.selectCountParkingRow();
    }

    /* AOP를 적용하여 Transaction 처리를 한다 */
    @Override
    //@Transactional(rollbackFor = {Exception.class})
    public void insertRefreshParkingInfo() throws Exception{
        List<ParkingDto> parkingList = new ArrayList<>();
        List<ParkingDto> errParkingList = new ArrayList<>();
        System.out.println("새벽 1시가 되면 데이터를 한번 비우고 위도와 경도를 DB에 새로 구축한다.");
        parkingMapper.deleteAllParking();

        int i=0;

        //현재 143페이지까지 지원
        while(true){
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
                JSONObject header = (JSONObject) response.get("header");
                String resResultCode = (String)header.get("resultCode");
                if(resResultCode.equals("03")){
                    System.out.println("**********Total Page is " + i + "**********");
                    break;
                }
                JSONObject body = (JSONObject) response.get("body");
                JSONArray items = (JSONArray) body.get("items");

                for(int j=0; j<items.size(); j++) {
                    object = (JSONObject) items.get(j);

                    //위도 경도가 없는 데이터에 대해서는 입력을 하지 않는다.
                    if (((String) object.get("latitude")).length() != 0
                            && ((String) object.get("longitude")).length() != 0
                    //인증에 필요한 정보가 없는 데이터에 대해서는 입력을 하지 않는다.
                            && ((String) object.get("prkplceNo")).length() != 0
                            && ((String) object.get("referenceDate")).length() != 0) {

                        ParkingDto parkingDto = new ParkingDto();
                        parkingDto.setId(parkingList.size());
                        parkingDto.setParkingName((String) object.get("prkplceNm"));
                        parkingDto.setLatitude(Double.parseDouble((String) object.get("latitude")));
                        parkingDto.setLongitude(Double.parseDouble((String) object.get("longitude")));
                        parkingDto.setPhone((String) object.get("phoneNumber"));

                        parkingDto.setParkingFor((String) object.get("prkplceSe"));
                        parkingDto.setParkingStruct((String) object.get("prkplceType"));
                        parkingDto.setRdnmadr((String) object.get("rdnmadr"));
                        parkingDto.setLnmadr((String) object.get("lnmadr"));
                        parkingDto.setOpenDay((String) object.get("operDay"));
                        parkingDto.setParkingCnt((String) object.get("prkcmprt"));

                        parkingDto.setWeekOpen((String) object.get("weekdayOperOpenHhmm"));
                        parkingDto.setWeekClose((String) object.get("weekdayOperColseHhmm"));
                        parkingDto.setSatOpen((String) object.get("satOperOperOpenHhmm"));
                        parkingDto.setSatClose((String) object.get("satOperCloseHhmm"));
                        parkingDto.setHoliOpen((String) object.get("holidayOperOpenHhmm"));
                        parkingDto.setHoliClose((String) object.get("holidayCloseOpenHhmm"));

                        parkingDto.setParkingFreeInfo((String) object.get("parkingchrgeInfo"));
                        parkingDto.setBasicTime((String) object.get("basicTime"));
                        parkingDto.setBasicCharge((String) object.get("basicCharge"));
                        parkingDto.setAddUnitTime((String) object.get("addUnitTime"));
                        parkingDto.setAddUnitCharge((String) object.get("addUnitCharge"));
                        parkingDto.setDayChargeTime((String) object.get("dayCmmtktAdjTime"));
                        parkingDto.setDayCharge((String) object.get("dayCmmtkt"));
                        parkingDto.setMonthCharge((String) object.get("monthCmmtkt"));

                        parkingDto.setParkingNo((String) object.get("prkplceNo"));
                        parkingDto.setReferenceDate((String) object.get("referenceDate"));

                        if (((parkingDto.getRdnmadr() == null || parkingDto.getRdnmadr().length() == 0)
                                && (parkingDto.getLnmadr() == null || parkingDto.getLnmadr().length() == 0))) {
                            errParkingList.add(parkingDto);
                        } else {
                            parkingList.add(parkingDto);
                        }
                    }
                }
            }catch(ParseException pe){
                throw new RuntimeException("JSON Parsing is  Fail Caused : " + pe.getCause());
            }
        }

        if(errParkingList.size() > 0){
            System.out.println("================Check the No Parking Addr Dto Issue List================\n\n");
            for(ParkingDto dto : errParkingList){
                System.out.println(dto);
            }
        }

        System.out.println("***************Success Dto List***************");
        for(ParkingDto dto : parkingList){
            System.out.println(dto);
        }

        int orgDataSize = parkingMapper.selectCountParkingRow();
        System.out.println("Origin Total Page : " + orgDataSize);

        /* 하루 주차장 데이터 5000개 가까이 소멸 시 확인 후에 조정 필요 */
        if(parkingList.size() + 100 < orgDataSize){
            throw new RuntimeException("Origin Parking Table Size is less than API Data Size 100");
        }

        parkingMapper.insertParking(parkingList);
    }
}
