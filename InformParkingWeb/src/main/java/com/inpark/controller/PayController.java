package com.inpark.controller;

import com.inpark.service.PayService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes({"tid", "parkingNo"}) // 세션에 저장된 겂을 사용할때 쓰는 어노테이션, session에서 없으면 model까지 훑어서 찾아냄.
public class PayController {
    @Autowired
    private PayService payService;

    @RequestMapping(value = "/kakaopay", method= RequestMethod.POST)
    @ResponseBody
    public String kakaopay(String parkingNo, String parkingName, String parkingPrice, Model model){
        System.out.println("Controller Kakao Pay Start : " + parkingNo + ", " + parkingName + ", " + parkingPrice);
        String resp = payService.buyParkingWithKakaoPay(parkingNo, parkingName, parkingPrice);

        try {
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(resp);
            model.addAttribute("tid", (String)object.get("tid"));
            model.addAttribute("parkingNo", parkingNo);
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        return resp;
    }

    @GetMapping("/approveKakaopay")
    public String approveKakaoPay(@RequestParam("pg_token") String pgToken, @ModelAttribute("tid") String tid,
                                  @ModelAttribute("parkingNo") String parkingNo){
        System.out.println("Controller tid : " + tid + ", token : " + pgToken);
        payService.approveKakaoPay(tid, pgToken, parkingNo);
        return "redirect:/home";
    }
}
