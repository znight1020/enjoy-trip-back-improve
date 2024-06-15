package com.ssafy.enjoyTrip.user.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ssafy.enjoyTrip.util.image.ImageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ssafy.enjoyTrip.user.dao.UserDao;
import com.ssafy.enjoyTrip.user.dto.UserDto;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
	private final UserDao userDao;
	private final ImageService imageService;

	// 최소 8자 이상, 최대 16자 이하여야 합니다.
	// 최소 하나의 영문자 (대소문자 모두 허용) 또는 숫자가 포함되어야 합니다.
	// 최소 하나의 특수문자가 포함되어야 합니다.
	private final String passwordRegex = "^(?=.*[a-zA-Z\\d])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,16}$";

	private final Pattern pattern = Pattern.compile(passwordRegex);
	@Override
	public int userRegister(UserDto userDto) {
		String userPassword = userDto.getPassword();
		Matcher matcher = pattern.matcher(userPassword);
        if (matcher.matches()) {
            return userDao.userRegister(userDto);
        } else {
            return -1;
        }
	}

	@Override
	public UserDto userDetail(int userId) {
		return userDao.userDetail(userId);
	}

	@Override
	public int userUpdate(UserDto userDto) {
		return userDao.userUpdate(userDto);
	}

	@Override
	public int userDelete(int userId) {
		return userDao.userDelete(userId);
	}

	@Override
	public String findPassword(String email) {
		return userDao.findPassword(email);
	}

	@Override
	public String updateUserProfileImage(int userId, String preProfileImageUrl, MultipartFile multipartFile) throws IOException {
		String userProfileImageUrl = imageService.imageUpload("user", multipartFile);
		imageService.preUserProfileDelete(preProfileImageUrl);
		int ret = userDao.updateUserProfileImage(Map.of("userId", userId, "userProfileImageUrl", userProfileImageUrl));

		if (ret == 1) {
			return userProfileImageUrl;
		} else {
			return null;
		}
	}
}
