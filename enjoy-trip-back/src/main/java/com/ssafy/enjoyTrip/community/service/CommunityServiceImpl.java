package com.ssafy.enjoyTrip.community.service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ssafy.enjoyTrip.community.dao.CommunityDao;
import com.ssafy.enjoyTrip.community.dto.CommunityDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.community-bucket}")
    private String bucket;

    static CommunityDto sorted[];
    private final CommunityDao communityDao;

    @Override
    public int communityInsert(CommunityDto dto) {
        return communityDao.communityInsert(dto);
    }

    @Override
    public int communityUpdate(CommunityDto dto) {
        return communityDao.communityUpdate(dto);
    }

    @Override
    public int communityDelete(int communityId) {
        return communityDao.communityDelete(communityId);
    }

    @Override
    public CommunityDto communityDetail(int communityId, int userId) { // 현재 세션 사용자의 userId
        CommunityDto communityDto = communityDao.communityDetail(communityId);
        if (communityDto.getUserId() == userId) {
            communityDto.setSameUser(true);
        } else {
            communityDto.setSameUser(false);
        }

        return communityDto;
    }

    @Override
    public List<CommunityDto> communityList(int limit, int offset) {
        List<CommunityDto> list = communityDao.communityList(limit, offset);
        return list;
    }

    @Override
    public int communityListTotalCnt() {
        return communityDao.communityListTotalCnt();
    }

    @Override
    public List<CommunityDto> communityListSearchWord(int limit, int offset, String searchWord) {
        return communityDao.communityListSearchWord(limit, offset, searchWord);
    }

    @Override
    public int communityListSearchWordTotalCnt(String searchWord) {
        return communityDao.communityListSearchWordTotalCnt(searchWord);
    }

    @Override
    public List<CommunityDto> communityListTop(int limit) {
        ArrayList<CommunityDto> dto = (ArrayList<CommunityDto>) communityDao.communityListTop(limit);
        Collections.sort(dto, (o1, o2) -> o1.getReadCount() - o2.getReadCount());
        System.out.println("service : " + dto);
        ArrayList<CommunityDto> sortDto = (ArrayList<CommunityDto>) sort(dto);
        
        return sortDto.stream().limit(limit).collect(Collectors.toList());
    }

    @Override
    public String uploadImage(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename();
        int dotIndex = originalFilename.lastIndexOf('.');

        String filename = originalFilename.substring(0, dotIndex);
        String extension = originalFilename.substring(dotIndex+1);
        String uuid = UUID.randomUUID().toString();
        String newFilename = filename + uuid + "." + extension;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(image.getSize());
        metadata.setContentType(image.getContentType());

        try {
            amazonS3Client.putObject(bucket, newFilename, image.getInputStream(), metadata);
        } catch (Exception e) {
            throw new IOException("Failed to upload file to S3", e);
        }
        // Return both URL and filename with UUID
        return amazonS3Client.getUrl(bucket, newFilename).toString();
    }

    @Override
    public int deleteImage(String imageName) {
        try {
            boolean isObjectExist = amazonS3Client.doesObjectExist(bucket, imageName);
            if (isObjectExist) {
                amazonS3Client.deleteObject(bucket, imageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    @Override
    public int hit(int communityId) {
        int data = communityDao.readHit(communityId) + 1;
        return communityDao.hit(data, communityId);
    }

    @Override
    public List<CommunityDto> specificUserWriteCommunity(int userId) {
        return communityDao.specificUserWriteCommunity(userId);
    }

    @Override
    public int deleteCommunityImage(String imageUrl) {
        int result = -1;

        if(("".equals(imageUrl) || "default".equals(imageUrl) || imageUrl == null)){
            return -1;
        }
        try {
            boolean isImageExist = amazonS3Client.doesObjectExist(bucket, imageUrl);
            if(isImageExist) {
                amazonS3Client.deleteObject(bucket, imageUrl);
                return 1;
            }
        } catch (Exception e) {
            return -1;
        }
        return result;
    }

    public List<CommunityDto> sort(List<CommunityDto> list) {
        // 사이즈가 1보다 크다면
        if (list.size() > 1) {
            // 왼쪽 오른쪽을 merge 합니다.
            return merge(
                    // 왼쪽 / 오른쪽으로 배열을 나누고 다시 sort하도록 합니다.
                    sort(list.subList(0, list.size() / 2)), sort(list.subList(list.size() / 2, list.size())));
        } else {
            // 사이즈가 1 이하라면 재귀가 종료됩니다.
            return list;
        }
    }

    /**
     * 병합합니다.
     *
     * @param list  왼쪽 배열
     * @param list2 오른쪽 배열
     * @return
     */
    public List<CommunityDto> merge(List<CommunityDto> list, List<CommunityDto> list2) {
        // 결과가 될 임시 배열입니다.
        List<CommunityDto> result = new ArrayList<>();
        int rightIdx = 0;

        // 왼쪽 배열을 순환하면서
        for (CommunityDto l : list) {

            // right를 끝까지 돌았는지 / right배열의 값이 l보다 작은지 확인하고,
            while (list2.size() > rightIdx && l.getReadCount() >list2.get(rightIdx).getReadCount()) {
                // 작은 값을 결과에 넣습니다.
                result.add(list2.get(rightIdx));
                rightIdx++;
            }
            // left가 작다면 left element를 넣습니다.
            result.add(l);
        }
        // 오른쪽 배열의 남은 숫자를 결과에 넣습니다.
        for (int i = rightIdx; i < list2.size(); i++) {
            result.add(list2.get(i));
        }
        return result;
    }
}