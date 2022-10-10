package capstone.jejuTourrecommend.config.security.filter;

import capstone.jejuTourrecommend.authentication.presentation.dto.request.LoginForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CustomLoginProcessingAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public CustomLoginProcessingAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        //여기서는 requestbody 내용 잘 들어감
        LoginForm loginForm = getLoginForm(request);
        String email = loginForm.getEmail();
        String password = loginForm.getPassword();


        //여기서 빈칸 예외처리
        if (!StringUtils.hasText(email) || !StringUtils.hasText(password)) {
            throw new AuthenticationServiceException("Username or Password not provided");
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);

        return super.getAuthenticationManager().authenticate(authRequest);


    }

    private LoginForm getLoginForm(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();

        LoginForm loginForm = objectMapper.readValue(messageBody, LoginForm.class);
        return loginForm;
    }



}
