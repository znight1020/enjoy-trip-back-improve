package com.ssafy.enjoyTrip.util.image;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.user-bucket}")
    String userBucketName;

    @Override
    public String imageUpload(String target, MultipartFile multipartFile) throws IOException {
        String bucketName = setBucket(target);
        if(bucketName == null) throw new NullPointerException("해당되는 버킷이 없습니다.");

        String newFilename = generateFileName(multipartFile);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3Client.putObject(bucketName, newFilename, multipartFile.getInputStream(), metadata);
        } catch (Exception e) {
            throw new IOException("Failed to upload file to S3", e);
        }

        return amazonS3Client.getUrl(bucket, newFilename).toString();
    }

    @Override
    public Integer imageDelete(String target, String fileName) {
        String bucketName = setBucket(target);
        if(bucketName == null) throw new NullPointerException("해당되는 버킷이 없습니다.");

        int result = -1;
        if(("".equals(fileName) || fileName == null)){
            return result;
        }
        try {
            boolean isImageExist = amazonS3Client.doesObjectExist(bucketName, fileName);
            if(isImageExist) {
                amazonS3Client.deleteObject(bucketName, fileName);
                result = 1;
            }
        } catch (Exception e) {
            log.info("Error", e);
        }
        return result;
    }

    @Override
    public void preUserProfileDelete(String preProfileImageUrl) {
        if(!("default".equals(preProfileImageUrl) || preProfileImageUrl == null || "".equals(preProfileImageUrl))){
            try {
                boolean isImageExist = amazonS3Client.doesObjectExist(userBucketName, preProfileImageUrl);
                if (isImageExist) {
                    amazonS3Client.deleteObject(bucket, preProfileImageUrl);
                }
            } catch (Exception e) {
               log.info("Error", e);
            }
        }
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
