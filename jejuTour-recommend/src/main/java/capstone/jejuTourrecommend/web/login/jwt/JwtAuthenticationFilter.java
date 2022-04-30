package capstone.jejuTourrecommend.web.login.jwt;

import capstone.jejuTourrecommend.web.login.exceptionClass.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//권한 확인하는 필터
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    //이 Filter를 조금 더 확장하여 스프링에서 제공하는 필터가 있는데 그것이 바로 GenericFilterBean이다

    private final JwtTokenProvider jwtTokenProvider;

    //이 필터가 요청을 가로채서 jwt토근이 유효한지 판단한다, 유효하면 다시 요청을 진행한다
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        //String accessToken = httpRequest.getHeader("X-AUTH-TOKEN");

        // 헤더에서 JWT 를 받아옵니다.
        String accessToken = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        try {
            log.info("인증 체크 필터 시작 {}", requestURI);
            // 유효한 토큰인지 확인합니다.
            if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
                // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
                // 이 함수 안에 loadUserByUsername 있음
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

                //인증이 성공하면 스프링이 관리하는 SecurityContext 에 Authentication 인증 객체를 저장합니다.
                //이객체는 반드시 Authentication 의 구현체만 가능하다
                SecurityContextHolder.getContext().setAuthentication(authentication);
                //Token에 대한 유효성을 검사하고, 만약 유효성 검증이 성공한다면, SecurityContextHolder에 해당 Authentication을 잡아주시면 됩니다.
                log.info("토큰이 유효하다");
            }
            //유효하지 않을경우 처리가 지금은 없은 나중에 심화 작업할때 할것임*******************
            //throw new  UserException("유효하지 않은 토큰입니다");//이거 안 먹음
            chain.doFilter(request, response);

        }catch (Exception e){
            httpResponse.setStatus(401);
            httpResponse.setHeader("ACCESS-TOKEN", accessToken);
            throw e;
        }finally {
            log.info("필터 종료!");
        }

    }
}

