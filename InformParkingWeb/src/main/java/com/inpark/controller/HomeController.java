package com.inpark.controller;

import com.inpark.dto.MemberDto;
import com.inpark.dto.UserDetailsDto;
import com.inpark.service.MemberService;

import com.inpark.service.UserDetailsServiceCustom;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


@Controller
@SessionAttributes({"tid"}) // 세션에 저장된 겂을 사용할때 쓰는 어노테이션, session에서 없으면 model까지 훑어서 찾아냄.
public class HomeController {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private MemberService memberService;

    @RequestMapping(value="/login"/*, method=RequestMethod.GET*/)
    public String login(HttpServletRequest request) {
        /*기존 컨트롤러에서 로그인 조건분기 처리를 해줬다면 이제는 spring-security에서 해주기 때문에
        컨트롤러에서는 화면만 분배해주는 메소드 하나만 작성하면 된다.
         */

        // 요청 시점의 사용자 URI 정보를 Session의 Attribute에 담아서 전달(잘 지워줘야 함)
        // 로그인이 틀려서 다시 하면 요청 시점의 URI가 로그인 페이지가 되므로 조건문 설정
        String uri = request.getHeader("Referer");
        System.out.println("uri : " + uri);
        if (!uri.contains("/login")) {
            request.getSession().setAttribute("prevPage",
                    request.getHeader("Referer"));
        }
        return "utility.login";
    }

    /*@RequestMapping(value="/login", method=RequestMethod.POST)
    public String postLogin(MemberDto dto, HttpServletRequest req){
        MemberDto res = memberService.loginMember(dto.getId());
        HttpSession session = req.getSession();
        System.out.println("로그인 컨트롤러인 여기를 거치나????");
        if(res == null){
            System.out.println("해당 아이디가 없음");
            return "utility.login";
        }

        System.out.println("로그인 생성 암호화 매칭 가즈아" + res.getPasswd() + res.getUserName());
        if(bCryptPasswordEncoder.matches(dto.getPasswd(), res.getPasswd())){
            System.out.println("로그인 성공");
            session.setAttribute("res", res);

            return "redirect:/";
        }else{
            System.out.println("비밀번호가 맞지 않음");
            return "utility.login";
        }
        return "utility.login";
    }*/

    /* security-context.xml에서 session 해제 설정을 했으니 따로 해줄 필요가 없다.
    @RequestMapping(value="/logout", method=RequestMethod.POST)
    public void postLogout(HttpServletRequest req){
        HttpSession session = req.getSession();
        session.invalidate();
        System.out.println("로그아웃 성공");
    }*/

    @RequestMapping(value="/join", method=RequestMethod.GET)
    public String join() {
        return "utility.join";
    }

    @RequestMapping(value="/join", method=RequestMethod.POST)
    public String postSignUp(MemberDto dto){
        memberService.insertMember(dto);
        return "utility.login";
    }

    @RequestMapping(value = "/memberIdChk", method=RequestMethod.POST)
    @ResponseBody
    public String memberIdChk(String id) {
        return memberService.selectIdCheck(id);
    }

    @RequestMapping(value = "/memberPhoneChk", method=RequestMethod.POST)
    @ResponseBody
    public String memberPhoneChk(String phone){
        return memberService.selectPhoneCheck(phone);
    }

    @RequestMapping("/home")
    public String home(Model model, Principal principal)
    {
        String id = null;

        if(principal == null){
            System.out.println("로그인 안한 상태!!!");
            id = "Anonymous";
        }else {
            System.out.println("로그인 상태!!!" + principal.getName());
            id = principal.getName();
            /*UserDetailsDto userDetails = (UserDetailsDto) userDetailsService.loadUserByUsername(id);
            model.addAttribute("userName", userDetails.getUsername2());
            System.out.println(userDetails.getUsername2());*/
        }
        model.addAttribute("userid", id);

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