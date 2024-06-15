package com.ssafy.enjoyTrip.user.service;

import com.ssafy.enjoyTrip.user.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
	int userRegister(UserDto userDto);
	UserDto userDetail(int userId);
	int userUpdate(UserDto userDto);
	int userDelete(int userId);
	String findPassword(String email);
	String updateUserProfileImage(int userId, String preProfileImageUrl, MultipartFile file) throws IOException;
}
