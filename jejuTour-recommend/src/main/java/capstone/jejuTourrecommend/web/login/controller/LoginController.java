package capstone.jejuTourrecommend.web.login.controller;

import capstone.jejuTourrecommend.domain.Member;
import capstone.jejuTourrecommend.web.GlobalDto;
import capstone.jejuTourrecommend.web.login.LoginUser;
import capstone.jejuTourrecommend.web.login.jwt.service.LoginService;
import capstone.jejuTourrecommend.web.login.dto.JoinDto;
import capstone.jejuTourrecommend.web.login.dto.LoginDto;
import capstone.jejuTourrecommend.web.login.dto.UserDto;
import capstone.jejuTourrecommend.web.login.exceptionClass.UserException;
import capstone.jejuTourrecommend.web.login.jwt.provider.JwtTokenProvider;
import capstone.jejuTourrecommend.web.login.dto.form.JoinForm;
import capstone.jejuTourrecommend.web.login.dto.form.LoginForm;
import capstone.jejuTourrecommend.web.login.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    private final JwtTokenProvider jwtTokenProvider;

    private final LoginService loginService;

    /**
     * RequestBody 로
     */
    //회원가입 (참고)회원가입할때는 accesstoken 반환할 필요 없음
    @PostMapping("/join")
    public JoinDto join(@Valid @RequestBody JoinForm form) {


        log.info("username={} ,email={}, password={}",form.getUsername(),
                form.getEmail(),form.getPassword());

        UserDto userDto = loginService.join(form);


        return new JoinDto(200,true,"회원가입 성공",userDto);

    }

    @GetMapping("/logout")
    public GlobalDto logout(@RequestHeader("ACCESS-TOKEN") String accesstoken,
                            @LoginUser Member member) {

        loginService.logout(accesstoken, member.getEmail());

        return new GlobalDto(200l, true, "로그아웃 성공");
    }

    @GetMapping("/deleteUser")
    public GlobalDto deleteUser(@RequestHeader("ACCESS-TOKEN") String accesstoken,
                                @LoginUser Member member) {

        loginService.deleteMember(member, accesstoken);

        return new GlobalDto(200l, true, "회원탈퇴 성공");
    }




    /**
     * 파라미터 버전
     */
    //회원가입하기
    //@PostMapping("/join")//@valid 가 오류를 잡아줘서  bindingResult에 들어감
    public JoinDto join1(@Valid @ModelAttribute JoinForm form, BindingResult bindingResult,
                         HttpServletResponse response) throws IOException {

        //여기서 실패하면 이렇게가 실으면 따로 로그인만의 오류 클래스 따로 만들어도됨
        if (bindingResult.hasErrors()) {//공백 예외 처리
            log.info("errors = {}", bindingResult);
            //오류 상태 따로 클래스로 만들어도 좋음
            //bindingResult.reject("오류오류오륭ㅇ로", new ErrorResult(200,false,"sss"),"sss");
            String defaultMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            throw new UserException(defaultMessage);
        }

        log.info("username={} ,email={}, password={}", form.getUsername(),
                form.getEmail(), form.getPassword());

        UserDto userDto = loginService.join(form);

        //회원가입할때는 accesstoken반환할 필요 없지
        //String accesstoken = jwtTokenProvider.createToken(userDto.getEmail(), userDto.getRole());

        return new JoinDto(200, true, "회원가입 성공", userDto);
    }

    //로그인하기
    //@PostMapping("/login")
    public LoginDto login1(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
                           HttpServletResponse response) throws IOException {

        log.info("email={}, password={}",form.getEmail(),form.getPassword());


        //여기서 실패하면 이렇게가 실으면 따로 로그인만의 오류 클래스 따로 만들어도됨
        if(bindingResult.hasErrors()){//공백 예외처리
            log.info("errors={}",bindingResult);
            String defaultMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            throw new UserException(defaultMessage);
        }

        UserDto userDto = loginService.login(form.getEmail(), form.getPassword());

        //로그인할때 이제서야 accestoken를 넘겨줌
        String accesstoken = jwtTokenProvider.createToken(userDto.getEmail(), userDto.getRole());

        return new LoginDto(200,true,"로그인 성공",userDto,accesstoken);

    }

    @GetMapping("/token/refresh")
    public TokenResponse refreshToken(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {

        return loginService.issueAccessToken(request);


    }

    //Access Token을 재발급 위한 api
//    @PostMapping("/issue")
//    public ResponseEntity issueAccessToken(HttpServletRequest request) throws Exception {
//        return ResponseEntity
//                .ok()
//                .body(userService.issueAccessToken(request));

//    }






}










