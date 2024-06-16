package com.ssafy.enjoyTrip.util.image.repository;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.web.multipart.MultipartFile;

public interface ImageRepository {
    String imageUpload(String bucketName, MultipartFile multipartFile, String fileName);
    Integer imageDelete(String target, String fileName);

    void preUserProfileDelete(String bucketName, String preProfileImageUrl);
}
