package com.ssafy.enjoyTrip.auth.service;

import com.ssafy.enjoyTrip.user.dto.UserDto;
import com.ssafy.enjoyTrip.auth.dao.LoginDao;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{
	
	private final LoginDao loginDao;
	
	@Override
	public UserDto login(String email, String password) {
		UserDto userDto = loginDao.login(email);

		if( userDto != null && userDto.getPassword().equals(password)) {
            userDto.setPassword(null);
            return userDto;
        }
		return null;
	}
}
