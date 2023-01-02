package com.inpark.controller;

import com.inpark.dto.MemberDto;
import com.inpark.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String login() {
        return "utility.login";
    }

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String postLogin(MemberDto dto){
        MemberDto res = memberService.loginMember(dto.getId());
        if(res == null){
            return "utility.login";
        }

        System.out.println("로그인 생성 암호화 매칭 가즈아" + res.getPasswd() + res.getUserName());
        if(bCryptPasswordEncoder.matches(dto.getPasswd(), res.getPasswd())){
            System.out.println("성공?");
            return "utility.login";
        }else{
            System.out.println("실패?");
            return "utility.login";
        }

    }

    @RequestMapping(value="/join", method=RequestMethod.GET)
    public String join() {
        return "utility.join";
    }

    @RequestMapping(value="/join", method=RequestMethod.POST)
    public String postSignUp(MemberDto dto){

        System.out.println("암호화 전 : " + dto.getPasswd());
        dto.setPasswd(bCryptPasswordEncoder.encode(dto.getPasswd()));
        System.out.println("암호화 후 : " + dto.getPasswd());

        memberService.insertMember(dto);

        return "redirect:/";
    }

    @RequestMapping(value = "/memberIdChk", method=RequestMethod.POST)
    @ResponseBody
    public String memberIdChk(String id) {
        System.out.println("id 중복체크 컨트롤러 진입" + id);
        int result = memberService.idCheck(id);
        System.out.println("result : " + result);
        if(result != 0){
            return "fail";
        } else{
            return "success";
        }
    }

    @RequestMapping(value = "/memberPhoneChk", method=RequestMethod.POST)
    @ResponseBody
    public String memberPhoneChk(String phone){
        System.out.println("phone 중복 체크" + phone);
        int result = memberService.phoneCheck(phone);
        System.out.println("result : " + result);
        if(result != 0){
            return "fail";
        } else {
            return "success";
        }
    }

    @RequestMapping("/home")
    public String home(){
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
