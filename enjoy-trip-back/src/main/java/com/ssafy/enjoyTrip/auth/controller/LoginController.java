package com.ssafy.enjoyTrip.auth.controller;

import java.util.Map;

import com.ssafy.enjoyTrip.SessionConst;
import com.ssafy.enjoyTrip.user.dto.UserDto;
import com.ssafy.enjoyTrip.auth.service.LoginService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "02. 로그인 관련 컨트롤러 페이지", description = "인증, 회원 관련 api")
public class LoginController {
	
	private final LoginService service;
	
	@PostMapping("/login")
	@Operation(summary = "로그인", description = "User 로그인 기능입니다.")
	public ResponseEntity<Map<String, Object>> login(@Validated @RequestParam("email") String email,
													 @Validated @RequestParam("password") String password,
													 HttpSession session){
		UserDto loginMember = service.login(email, password);

		if (loginMember != null) {
			session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
			return new ResponseEntity<>(Map.of("result", "success", "user", loginMember), HttpStatus.OK);
		}
		return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/logout")
	@Operation(summary = "로그아웃", description = "User 로그아웃 기능입니다.")
	public ResponseEntity<Map<String, Object>> logout(HttpSession session){
		session.invalidate();
		return new ResponseEntity<>(Map.of("result", "success"), HttpStatus.OK);
	}
	@GetMapping("/check")
	@Operation(summary = "사용자 인증", description = "User 로그인 인증")
	public ResponseEntity<String> checkAuth(HttpSession session) {
		if (session != null && session.getAttribute(SessionConst.LOGIN_MEMBER) != null) {
			return ResponseEntity.ok("Authenticated");
		} else {
			return ResponseEntity.status(401).body("Unauthorized");
		}
	}
}
