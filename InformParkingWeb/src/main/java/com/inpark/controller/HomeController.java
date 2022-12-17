package com.inpark.controller;

import com.inpark.dto.MemberDto;
import com.inpark.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Autowired
    private MemberService memberService;

    @RequestMapping("/hello")
    public String showHome(){
        return "home";
    }

    @RequestMapping("/loginResultView/{id}")
    /*@PathVariable을 통해 url에 적힌 변수의 값을 가져올 수 있다.
    여러개로 작성할 수 있으며 아래처럼 선언 이외 @PathVariable(id) String id로도 입력가능하다.*/
    public String loginTestResult(@PathVariable String id, Model model){
        MemberDto dto = memberService.selectMember(id);
        model.addAttribute("member", dto);
        return "loginResultView";
    }
}
