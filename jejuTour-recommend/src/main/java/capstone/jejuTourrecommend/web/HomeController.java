package capstone.jejuTourrecommend.web;

import capstone.jejuTourrecommend.domain.Member;
import capstone.jejuTourrecommend.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {


    //홈화면으로 가는데 로그인회원과 비로그인 회원을 다르게 화면을 보여주는 작업임
    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model){


        //세션에 회원데이터가 없으면 home
        if(loginMember==null){
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member",loginMember);
        return "loginHome";
    }

}
