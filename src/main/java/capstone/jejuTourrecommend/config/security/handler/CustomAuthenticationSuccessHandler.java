package capstone.jejuTourrecommend.config.security.handler;

import capstone.jejuTourrecommend.config.security.dto.UserDto;
import capstone.jejuTourrecommend.config.security.provider.JwtTokenProvider;
import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.authentication.infrastructure.respository.MemberJpaRepository;
import capstone.jejuTourrecommend.authentication.presentation.dto.response.LoginDto;
import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private final JwtTokenProvider jwtTokenProvider;

    //private final LoginService loginService;
    private final MemberJpaRepository memberJpaRepository;
    //private final CustomUserDetailService customUserDetailService;

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        User user = (User) authentication.getPrincipal();
        String email = user.getUsername();

        log.info("email = {} ", email);

        // request 로 회원 이메일 못 얻음!! 비어 있음 mapper 로 log 찍어 보니깐
        //LoginForm loginForm = getLoginForm(request);
        //String email = loginForm.getEmail();

        Member member = memberJpaRepository.findOptionByEmail(email)
                .orElseThrow(() -> new UserException("가입되지 않은 E-MAIL 입니다."));

        log.info("member.getCreatedDate() = " + member.getCreatedDate().toString());

        UserDto userDto = new UserDto(
                member.getId(),member.getUsername(),member.getEmail(),member.getRole()
                ,member.getCreatedDate(),member.getLastModifiedDate());


        //로그인할때 이제서야 accestoken를 넘겨줌
        String accesstoken = jwtTokenProvider.createToken(userDto.getEmail(), userDto.getRole());

        LoginDto loginDto = new LoginDto(200, true, "로그인 성공", userDto, accesstoken);

        //Todo : 이거 아예 objectmapper 를 빈드로 설정하고 registermodule 할수 있
        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.writeValue(response.getWriter(), loginDto);
    }



}
