package capstone.jejuTourrecommend.web.login;

import capstone.jejuTourrecommend.domain.Member;
import capstone.jejuTourrecommend.domain.Service.LoginService;
import capstone.jejuTourrecommend.web.SessionConst;
import capstone.jejuTourrecommend.web.login.form.LoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form){
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL,
                        HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        //LoginForm에 @Data를 해줘야 get으로 접근 가능
        Member loginMember = loginService.login(form.getEmail(), form.getPassword());

        if(loginMember==null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }


        //로그인 성공
        //세션이 있으면 있는 세션을 반환, 없으면 신규 세션을 생성해서 반환함
        HttpSession session = request.getSession();
        //세션의 로그인 회원정보를 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);

        //세션 관리자를 통해 세션을 생성하고, 회원데이터를 보관
        //sessionManager.createSession(loginMember,response );

        return "redirect:/";

    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request){

        HttpSession session = request.getSession(false);

        if(session!=null){
            session.invalidate();
        }
        return "redirect:/";
    }

}
