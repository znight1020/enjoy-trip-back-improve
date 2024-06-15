package com.ssafy.enjoyTrip.common.filter;

import com.ssafy.enjoyTrip.SessionConst;
import com.ssafy.enjoyTrip.user.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class LoginCheckFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("로그인을 한 사용자의 요청")
    void loginUserRequest() throws Exception {
        // Given
        MockHttpSession session = new MockHttpSession();
        UserDto user = new UserDto();
        user.setUserId(10000);

        String requestURI = "/user/10000"; // 사용자 정보 조회 기능은 로그인 한 사용자만 요청 가능

        session.setAttribute(SessionConst.LOGIN_MEMBER, user); // 세션에 사용자 정보 추가

        // When
        mockMvc.perform(MockMvcRequestBuilders.get(requestURI).session(session))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("로그인을 하지 않은 사용자의 요청")
    void noneLoginUserRequest() throws Exception {
        // Given
        String requestURI = "/user/10000"; // 사용자 정보 조회 기능은 로그인 한 사용자만 요청 가능

        // When
        mockMvc.perform(MockMvcRequestBuilders.get(requestURI))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("fail"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.redirect").value("login"));
    }
}
