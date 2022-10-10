package capstone.jejuTourrecommend.authentication.presentation;

import capstone.jejuTourrecommend.authentication.domain.service.LoginService;
import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.common.GlobalDto;
import capstone.jejuTourrecommend.authentication.LoginUser;
import capstone.jejuTourrecommend.authentication.presentation.dto.response.JoinDto;
import capstone.jejuTourrecommend.config.security.dto.UserDto;
import capstone.jejuTourrecommend.authentication.presentation.dto.request.JoinForm;
import capstone.jejuTourrecommend.config.security.provider.JwtTokenProvider;
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










