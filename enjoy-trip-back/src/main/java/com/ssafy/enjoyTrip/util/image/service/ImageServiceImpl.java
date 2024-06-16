package com.ssafy.enjoyTrip.util.image.service;

import com.ssafy.enjoyTrip.util.image.repository.ImageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.user-bucket}")
    String userBucketName;

    @Override
    public String imageUpload(String target, MultipartFile multipartFile) {
        String bucketName = setBucket(target);
        String newFilename = generateFileName(multipartFile);

        return imageRepository.imageUpload(bucketName, multipartFile, newFilename);
    }

    @Override
    public Integer imageDelete(String target, String fileName) {
        String bucketName = setBucket(target);

        return imageRepository.imageDelete(bucketName, fileName);
    }

    @Override
    public void preUserProfileDelete(String preProfileImageUrl) {
        imageRepository.preUserProfileDelete(userBucketName, preProfileImageUrl);
    }

    private String setBucket(String target) {
        String bucketName = null;
        if("user".equals(target)) bucketName = bucket + "/user";
        else if("meeting".equals(target)) bucketName = bucket + "/meeting";
        else if("community".equals(target)) bucketName = bucket + "/community";

        return bucketName;
    }

    private static String generateFileName(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename(); // 파일 원본 이름
        int dotIndex = originalFilename.lastIndexOf('.');

        String filename = originalFilename.substring(0, dotIndex); // 파일 이름
        String extension = originalFilename.substring(dotIndex + 1); // 파일 확장자
        String uuid = UUID.randomUUID().toString(); // uuid 생성

        return filename + uuid + "." + extension;
    }
}
