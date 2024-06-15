package com.ssafy.enjoyTrip.common.filter;

import com.ssafy.enjoyTrip.SessionConst;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("LoginFilter init");
    }

    private static final String[] whiteList = {
            "/", "/user", "/user/find/*", "/auth/login", "/auth/logout", "/ping", "/favorite/*", "/favorite",

            "/css/*", "/ico.*",

            "/notice/*", "/trip/*", "/community/communityListTop", "/travel/travelListTop"};
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("LoginFilter 시작");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();


        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            if(isLoginCheckPath(requestURI)) {
                HttpSession session = httpRequest.getSession(false);

                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null){
                    log.info("로그인 필요");
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return; // 여기가 중요, 미인증 사용자는 다음으로 진행하지 않고 끝
                }
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e; // 예외 로깅 가능 하지만, 톰캣까지 예외를 보내주어야 함
        }
    }

    private boolean isLoginCheckPath(String requestURI){
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }

}
