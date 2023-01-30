package com.inpark.controller;

import com.inpark.dto.PayDto;
import com.inpark.service.PayService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@SessionAttributes({"tid", "parkingNo", "payDto"}) // 세션에 저장된 겂을 사용할때 쓰는 어노테이션, session에서 없으면 model까지 훑어서 찾아냄.
public class PayController {
    @Autowired
    private PayService payService;

    @RequestMapping(value = "/kakaopay", method= RequestMethod.POST)
    @ResponseBody
    public String kakaopay(String parkingNo, String parkingName, int dayMonthFlag, String parkingPrice,
                         String carNum, Model model, Principal principal){
        String dayMonthStr = dayMonthFlag == 0 ? new String("일 정기권") : new String("월 정기권");
        String resp = payService.buyParkingWithKakaoPay(parkingNo, parkingName + "_" + dayMonthStr, parkingPrice);

        // 결제 승인에 정보를 넘기기위한 dto를 조합한다.
        try {
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(resp);
            model.addAttribute("tid", (String)object.get("tid"));
            model.addAttribute("parkingNo", parkingNo);

            PayDto payDto = new PayDto();

            payDto.setParkingName(parkingName);
            payDto.setDayMonth(dayMonthStr);
            payDto.setParkingPrice(parkingPrice);
            payDto.setUsers_id(principal.getName());
            payDto.setCarNum(carNum);

            model.addAttribute("payDto", payDto);
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        return resp;
    }

    @GetMapping("/approveKakaopay")
    public String approveKakaoPay(@RequestParam("pg_token") String pgToken, @ModelAttribute("tid") String tid,
                                  @ModelAttribute("parkingNo") String parkingNo,
                                  @ModelAttribute("payDto") PayDto payDto){
        payService.approveKakaoPay(tid, pgToken, parkingNo, payDto);
        return "redirect:/";
    }

    @RequestMapping(value = "/getInitSettingPurchaseHistory", method= RequestMethod.POST)
    @ResponseBody
    public String getInitSettingPurchaseHistory(Principal principal){
        return payService.selectPurchaseHistory(principal.getName());
    }
}
