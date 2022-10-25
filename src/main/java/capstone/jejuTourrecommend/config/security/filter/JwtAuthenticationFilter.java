package capstone.jejuTourrecommend.config.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.config.redis.LogoutAccessTokenRedisRepository;
import capstone.jejuTourrecommend.config.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 권한 확인하는 필터
 * 이 Filter를 조금 더 확장하여 스프링에서 제공하는 필터가 있는데 그것이 바로 GenericFilterBean이다
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;

	//이 필터가 요청을 가로채서 "jwt 토근" 이 유효한지 판단한다, 유효하면 다시 요청을 진행한다
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String accessToken = request.getHeader("ACCESS-TOKEN");

		if (accessToken != null) {
			checkLogout(accessToken);
			if (jwtTokenProvider.validateToken(accessToken)) {
				Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

				SecurityContextHolder.getContext().setAuthentication(authentication);
				log.info("토큰이 유효하다");
			}
		}

		filterChain.doFilter(request, response);

	}

	//필터에 예외 잘 됨
	private void checkLogout(String accessToken) {
		if (logoutAccessTokenRedisRepository.existsById(accessToken)) {
			throw new UserException("이미 로그아웃된 회원입니다.");
		}
	}

}

