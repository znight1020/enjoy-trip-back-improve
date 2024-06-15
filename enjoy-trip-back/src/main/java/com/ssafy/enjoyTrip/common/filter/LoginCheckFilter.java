package com.ssafy.enjoyTrip.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.enjoyTrip.SessionConst;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class LoginCheckFilter implements Filter {
    private static final String[] whiteList = {
            "/ping", // 서버-클라이언트 통신 테스트
            "/", "/user", "/user/find/*", "/auth/login", "/auth/logout", // 사용자 회원가입, 로그인, 찾기
            "/notice/*", // 공지사항
            "/community/communityListTop", "/travel/travelListTop"}; // 인기 게시물

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        try {
            if(isLoginCheckPath(requestURI)) {
                HttpSession session = httpRequest.getSession(false);

                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null){
                    httpResponse.setContentType("application/json");
                    httpResponse.setStatus(HttpServletResponse.SC_FOUND);
                    httpResponse.getWriter().write(new ObjectMapper().writeValueAsString(
                            Map.of("result", "fail", "redirect", "login")
                    ));
                    return;
                }
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e;
        }
    }

    private boolean isLoginCheckPath(String requestURI){
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }
}
