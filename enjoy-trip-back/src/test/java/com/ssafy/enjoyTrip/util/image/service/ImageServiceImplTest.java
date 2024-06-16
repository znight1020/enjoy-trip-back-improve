package com.ssafy.enjoyTrip.util.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.ssafy.enjoyTrip.common.exception.ImageUploadException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.*;

@SpringBootTest
class ImageServiceImplTest {

    @Autowired private ImageService imageService;

    private MockMultipartFile multipartFile;
    @Autowired
    private AmazonS3Client amazonS3Client;

    @Test
    @DisplayName("이미지 업로드 성공 테스트")
    void testImageUpload_Success() throws IOException {
        // given
        multipartFile = new MockMultipartFile(
                "test",
                "1.jpg",
                "image/jpg",
                new FileInputStream(("C:\\Users\\LHS\\Desktop\\1.jpg"))
        ); // 임의의 MultiPartFile 생성

        // when
        String result = imageService.imageUpload("user", multipartFile); // 이미지 저장 경로 반환

        // then
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("이미지 업로드 실패 테스트 1 - 존재하지 않는 버킷")
    void testImageUpload_Fail() throws IOException {
        // given
        multipartFile = new MockMultipartFile(
                "test",
                "1.jpg",
                "image/jpg",
                new FileInputStream(("C:\\Users\\LHS\\Desktop\\1.jpg"))
        ); // 임의의 MultiPartFile 생성

        // when
        Assertions.assertThatThrownBy(() -> imageService.imageUpload("empty", multipartFile))
                .isInstanceOf(ImageUploadException.class);
    }
}

