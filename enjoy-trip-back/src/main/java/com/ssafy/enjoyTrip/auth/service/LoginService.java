package com.ssafy.enjoyTrip.auth.service;

import com.ssafy.enjoyTrip.user.dto.UserDto;

public interface LoginService {
	UserDto login(String email, String password);
}
