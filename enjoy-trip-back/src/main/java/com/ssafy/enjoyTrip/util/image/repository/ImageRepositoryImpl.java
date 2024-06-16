package com.ssafy.enjoyTrip.util.image.repository;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ssafy.enjoyTrip.common.exception.ImageUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Repository
@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepository {
    private final AmazonS3Client amazonS3Client;

    @Override
    public String imageUpload(String bucketName, MultipartFile multipartFile, String fileName) {
        ObjectMetadata metadata = setMetaData(multipartFile);

        try {
            amazonS3Client.putObject(bucketName, fileName, multipartFile.getInputStream(), metadata);
        } catch (AmazonServiceException e) {
            throw new ImageUploadException("요청 수행 실패, Message: " + e.getMessage() , e);
        } catch (SdkClientException e) {
            throw new ImageUploadException("사용자 오류 발생, Message: " + e.getMessage(), e);
        } catch (AmazonClientException e) {
            throw new ImageUploadException("요청 실패, Message: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new ImageUploadException("파일 입출력 오류 발생, Message: " + e.getMessage(), e);
        }

        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

    @Override
    public Integer imageDelete(String bucketName, String fileName) {
        try {
            boolean isImageExist = amazonS3Client.doesObjectExist(bucketName, fileName);
            if(isImageExist) amazonS3Client.deleteObject(bucketName, fileName);
            return 1;
        } catch (AmazonServiceException e) {
            throw new ImageUploadException("요청 수행 실패, Message: " + e.getMessage() , e);
        } catch (SdkClientException e) {
            throw new ImageUploadException("사용자 오류 발생, Message: " + e.getMessage(), e);
        } catch (AmazonClientException e) {
            throw new ImageUploadException("요청 실패, Message: " + e.getMessage(), e);
        }
    }

    @Override
    public void preUserProfileDelete(String bucketName, String preProfileImageUrl) {
        try {
            boolean isImageExist = amazonS3Client.doesObjectExist(bucketName, preProfileImageUrl);
            if (isImageExist) {
                amazonS3Client.deleteObject(bucketName, preProfileImageUrl);
            }
        } catch (AmazonServiceException e) {
            throw new ImageUploadException("요청 수행 실패, Message: " + e.getMessage() , e);
        } catch (SdkClientException e) {
            throw new ImageUploadException("사용자 오류 발생, Message: " + e.getMessage(), e);
        } catch (AmazonClientException e) {
            throw new ImageUploadException("요청 실패, Message: " + e.getMessage(), e);
        }
    }

    private static ObjectMetadata setMetaData(MultipartFile multipartFile) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        return metadata;
    }
}
