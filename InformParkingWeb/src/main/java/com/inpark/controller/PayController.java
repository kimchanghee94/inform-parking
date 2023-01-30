package com.inpark.controller;

import com.inpark.service.MemberService;
import com.inpark.service.PayService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes({"tid"}) // 세션에 저장된 겂을 사용할때 쓰는 어노테이션, session에서 없으면 model까지 훑어서 찾아냄.
public class PayController {
    @Autowired
    private PayService payService;

    @RequestMapping(value = "/kakaopay", method= RequestMethod.POST)
    @ResponseBody
    public String kakaopay(Model model){
        String resp = payService.buyParkingWithKakaoPay();

        try {
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(resp);
            model.addAttribute("tid", (String)object.get("tid"));
            System.out.println("kakaopay tid : " + (String)object.get("tid"));
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        return resp;
    }

    @GetMapping("/approveKakaopay")
    public String approveKakaoPay(@RequestParam("pg_token") String pgToken, @ModelAttribute("tid") String tid){
        System.out.println("Controller tid : " + tid + ", token : " + pgToken);
        payService.approveKakaoPay(tid, pgToken);
        return "redirect:/home";
    }
}
