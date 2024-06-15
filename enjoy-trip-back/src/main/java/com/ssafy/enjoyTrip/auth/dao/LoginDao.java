package com.ssafy.enjoyTrip.auth.dao;

import com.ssafy.enjoyTrip.user.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import com.ssafy.enjoyTrip.auth.dto.LoginDto;

@Mapper
public interface LoginDao {
	UserDto login(String userEmail);
}
