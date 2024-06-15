package com.ssafy.enjoyTrip.util.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String imageUpload(String target, MultipartFile multipartFile) throws IOException;
    Integer imageDelete(String target, String fileName);

    void preUserProfileDelete(String preProfileImageUrl);
}
