package capstone.jejuTourrecommend.web.login.jwt.handler;

import capstone.jejuTourrecommend.domain.Member;
import capstone.jejuTourrecommend.repository.MemberRepository;
import capstone.jejuTourrecommend.web.login.dto.LoginDto;
import capstone.jejuTourrecommend.web.login.dto.UserDto;
import capstone.jejuTourrecommend.web.login.exceptionClass.UserException;
import capstone.jejuTourrecommend.web.login.form.LoginForm;
import capstone.jejuTourrecommend.web.login.jwt.provider.JwtTokenProvider;
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
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private final JwtTokenProvider jwtTokenProvider;

    //private final LoginService loginService;
    private final MemberRepository memberRepository;
    //private final CustomUserDetailService customUserDetailService;

    private final ObjectMapper objectMapper = new ObjectMapper();

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

        Member member = memberRepository.findOptionByEmail(email)
                .orElseThrow(() -> new UserException("가입되지 않은 E-MAIL 입니다."));

        System.out.println("member.getCreatedDate() = " + member.getCreatedDate().toString());

        UserDto userDto = new UserDto(
                member.getId(),member.getUsername(),member.getEmail(),member.getRole()
                ,member.getCreatedDate(),member.getLastModifiedDate());


        //로그인할때 이제서야 accestoken를 넘겨줌
        String accesstoken = jwtTokenProvider.createToken(userDto.getEmail(), userDto.getRole());

        LoginDto loginDto = new LoginDto(200, true, "로그인 성공", userDto, accesstoken);

        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.writeValue(response.getWriter(), loginDto);
    }

    private LoginForm getLoginForm(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        log.info("inputStream = {} ",inputStream.toString());

        String messageBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        if (messageBody.isEmpty()) {
            System.out.println("messageBody = " + messageBody);
        }

        //String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody = {}",messageBody);
//        ObjectMapper objectMapper = new ObjectMapper();

        LoginForm loginForm = objectMapper.readValue(messageBody, LoginForm.class);
        return loginForm;
    }

}
