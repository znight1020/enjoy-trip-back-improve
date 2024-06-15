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
	private final AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.user-bucket}")
	private String bucket;
	private String userBucket;

	@PostConstruct
	private void init() {
		userBucket = bucket + "/user";
		log.info("userBucket: {}", userBucket);
	}


	private final UserDao userDao;

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
            System.out.println("비밀번호가 규칙에 맞습니다.");
    		return userDao.userRegister(userDto);
        } else {
            System.out.println("비밀번호가 규칙에 맞지 않습니다.");
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


	/**
	 * putObject() 메소드가 파일을 저장해주는 메소드
	 * getURl()을 통해 파일이 저장된 URL을 return 해주고, 이 URL로 이동 시 해당 파일이 오픈됨 (버킷 정책 변경을 하지 않았으면 파일은 업로드 되지만 해당 URL로 이동 시 accessDenied 됨)
	 * 만약 MultipartFile에 대해 잘 모르거나 웹 페이지에서 form으로 파일을 입력받고 싶다면 [Spring Boot] 파일 업로드 참고
	 * */
	@Override
	public String updateUserProfileImage(int userId, String preProfileImageUrl, MultipartFile multipartFile) throws IOException {
		log.info("userBucket: {}", userBucket);
		String originalFilename = multipartFile.getOriginalFilename();
		int dotIndex = originalFilename.lastIndexOf('.');

		String filename = originalFilename.substring(0, dotIndex);
		String extension = originalFilename.substring(dotIndex + 1);
		String uuid = UUID.randomUUID().toString();
		String newFilename = filename + uuid + "." + extension;

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(multipartFile.getSize());
		metadata.setContentType(multipartFile.getContentType());

		try {
			// S3에 파일 업로드
			amazonS3Client.putObject(bucket, newFilename, multipartFile.getInputStream(), metadata);
		} catch (Exception e) {
			// 업로드 실패 시 예외를 던져 이후 로직이 실행되지 않도록 함
			throw new IOException("Failed to upload file to S3", e);
		}

		if(!("default".equals(preProfileImageUrl) || preProfileImageUrl == null || "".equals(preProfileImageUrl))){
			try {
				boolean isObjectExist = amazonS3Client.doesObjectExist(bucket, preProfileImageUrl);
				if (isObjectExist) {
					amazonS3Client.deleteObject(bucket, preProfileImageUrl);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String userProfileImageUrl = amazonS3Client.getUrl(bucket, newFilename).toString();

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		paramMap.put("userProfileImageUrl", userProfileImageUrl);

		int ret = userDao.updateUserProfileImage(paramMap);

		if (ret == 1) {
			return userProfileImageUrl;
		} else {
			return null;
		}
	}
}
