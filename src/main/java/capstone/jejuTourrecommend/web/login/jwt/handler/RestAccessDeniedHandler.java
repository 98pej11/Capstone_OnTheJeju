package capstone.jejuTourrecommend.web.login.jwt.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final String MESSAGE = "권한이 없습니다.";

    /**
     * 익명사용자 접근이 아니고 사용권한이 없는 경우임
     * 익명사용자긔 접근 처리는 AjaxLoginAuthenticationEntryPoint 가 함
     * @param request
     * @param response
     * @param accessDeniedException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        response.sendError(HttpServletResponse.SC_FORBIDDEN, MESSAGE);
    }
}
