package capstone.jejuTourrecommend.web.login.controller;

import capstone.jejuTourrecommend.domain.Member;
import capstone.jejuTourrecommend.web.GlobalDto;
import capstone.jejuTourrecommend.web.login.LoginUser;
import capstone.jejuTourrecommend.web.login.dto.JoinDto;
import capstone.jejuTourrecommend.web.login.dto.UserDto;
import capstone.jejuTourrecommend.web.login.dto.form.JoinForm;
import capstone.jejuTourrecommend.web.login.jwt.provider.JwtTokenProvider;
import capstone.jejuTourrecommend.web.login.jwt.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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





}










