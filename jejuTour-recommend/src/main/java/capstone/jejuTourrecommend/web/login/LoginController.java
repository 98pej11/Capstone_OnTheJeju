package capstone.jejuTourrecommend.web.login;

import capstone.jejuTourrecommend.domain.Member;
import capstone.jejuTourrecommend.domain.Service.LoginService;
import capstone.jejuTourrecommend.web.jwt.JwtTokenProvider;
import capstone.jejuTourrecommend.web.login.form.JoinForm;
import capstone.jejuTourrecommend.web.login.form.LoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// Controller 에서 회원 가입과 로그인을 통한 인증 과정
@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    //private final UserRepository userRepository;

    private final LoginService loginService;

    /**
     *  객체로 정보 전달 버젼 @RequestBody
     */
    // 회원가입
    //@PostMapping("/join")
    public Long join(@Valid @RequestBody JoinForm form, BindingResult bindingResult) {

        log.info("username={} ,email={}, password={}",form.getUsername(),form.getEmail(),form.getPassword());

        return loginService.join(form);

//        return userRepository.save(
//                User.builder()
//                .email(user.get("email"))
//                .password(passwordEncoder.encode(user.get("password"))) //나 여기서 password를 토큰정보에 주지 않음
//                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
//                .build()
//                ).getId();

    }

    //지금 로그인 정보를 http body 로 줘서 @RequestBody로 한거고 요청파라미터로 주면 @ModelAttribute로 객체에 담으면 됨
    // 로그인
    //@PostMapping("/login")
    public String login(@Valid @RequestBody LoginForm form, BindingResult bindingResult
    , HttpServletResponse response) throws IOException {

        log.info("email={}, password={}",form.getEmail(),form.getPassword());

        //LoginForm에 @Data를 해줘야 get으로 접근 가능
        Member loginMember = loginService.login(form.getEmail(), form.getPassword());
        log.info("loginMember ={}",loginMember);
        if(loginMember==null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            response.sendError(404,"아이디 또는 비밀번호가 맞지 않습니다.");
        }

//        //가입된 유저인지 확인하는거
//        User member = userRepository.findByEmail(user.get("email"))
//                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
//
         //비밀번호가 맞는지 확인하는거
//        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
//            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
//        }

        //회원정보가 일치하면 토큰을 만들어서 반환한, 반환정보는 이메일과 권한정호이다
        // (User 객체에서 .getUsername 에 email 을 넣음)
        //return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());

        return jwtTokenProvider.createToken(loginMember.getEmail(),loginMember.getRole());

        //토큰으로 이메일 한번 뽑아봄
        //return jwtTokenProvider.getUserPk(jwtTokenProvider.createToken(loginMember.getEmail(),loginMember.getRole()));
    }


    /**
     * 파라미터 버전
     */
    @PostMapping("/join")//@valid 가 오류를 잡아줘서  bindingResult에 들어감
    public Long join1(@Valid @ModelAttribute JoinForm form, BindingResult bindingResult,
                        HttpServletResponse response) throws IOException {

        if(bindingResult.hasErrors()){//여기서 실패하면 이렇게가 실으면 따로 로그인만의 오류 클래스 따로 만들어도됨
            log.info("errors={}",bindingResult);
            //오류 상태 따로 클래스로 만들어도 좋음
            String defaultMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            response.sendError(404,defaultMessage);
        }

        log.info("username={} ,email={}, password={}",form.getUsername(),form.getEmail(),form.getPassword());

        return loginService.join(form);
    }

    @PostMapping("/login")
    public String login1(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
     HttpServletResponse response) throws IOException {

        log.info("email={}, password={}",form.getEmail(),form.getPassword());

        if(bindingResult.hasErrors()){//여기서 실패하면 이렇게가 실으면 따로 로그인만의 오류 클래스 따로 만들어도됨
            log.info("errors={}",bindingResult);
            String defaultMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            response.sendError(404,defaultMessage);
        }

        //LoginForm에 @Data를 해줘야 get으로 접근 가능
        Member loginMember = loginService.login(form.getEmail(), form.getPassword());
        log.info("loginMember ={}",loginMember);
        if(loginMember==null){
            //bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            response.sendError(404,"아이디 또는 비밀번호가 맞지 않습니다.");
        }

        return jwtTokenProvider.createToken(loginMember.getEmail(),loginMember.getRole());
    }

    @PostMapping("/user/test")
    public Map userResponseTest() {
        Map<String, String> result = new HashMap<>();
        result.put("result", "user ok");
        return result;
    }

    //회원가입시 기본 권한은 user 다
    @PostMapping("/admin/test")
    public Map adminResponseTest() {
        Map<String, String> result = new HashMap<>();
        result.put("result", "admin ok");
        return result;
    }



}
