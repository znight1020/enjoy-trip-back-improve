package com.ssafy.enjoyTrip.util.image.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;


@Repository
@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepository {

    @Override
    public String imageUpload(String bucketName, MultipartFile multipartFile, String fileName) {
        return "";
    }

    @Override
    public Integer imageDelete(String bucketName, String fileName) {
        return 0;
    }

    @Override
    public void preUserProfileDelete(String bucketName, String preProfileImageUrl) {

    }

}
