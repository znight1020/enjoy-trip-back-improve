package com.ssafy.enjoyTrip.meeting.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ssafy.enjoyTrip.meeting.dao.MeetingDao;
import com.ssafy.enjoyTrip.meeting.dao.ParticipantDao;
import com.ssafy.enjoyTrip.meeting.dto.MeetingDetailDto;
import com.ssafy.enjoyTrip.meeting.dto.MeetingDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.ssafy.enjoyTrip.meeting.dto.ParticipantDto;
import com.ssafy.enjoyTrip.user.dao.UserDao;
import com.ssafy.enjoyTrip.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService{

    @Value("${cloud.aws.s3.meeting-bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;
    private final MeetingDao meetingDao;
    private final ParticipantDao participantDao;
    private final UserDao userDao;

    @Override
    public int meetingInsert(MeetingDto dto) {
        return meetingDao.meetingInsert(dto);
    }

    @Override
    public MeetingDetailDto meetingDetail(int meetingId, UserDto dto) {
        MeetingDetailDto detailDto = new MeetingDetailDto();
        MeetingDto meetingDto = meetingDao.meetingDetail(meetingId);
        List<ParticipantDto> participantList= participantDao.joinedParticipants(meetingId);

        //모집글을 쓴 사람이 본인인지, 또는 현재 로그인한 사람이 관리자인지
        if(meetingDto.getUserId() == dto.getUserId()){
            meetingDto.setSameUser(true);
        }else{
            meetingDto.setSameUser(false);
            meetingDto.setAdmin(false);
        }
        if("1".equals(dto.getCode())){
            meetingDto.setAdmin(true);
        }

        detailDto.setMeetingDto(meetingDto);

        if(!participantList.isEmpty()){
            detailDto.setPartiList(participantList);
        }

        return detailDto;
    }

    @Override
    public List<MeetingDto> meetingList(int limit, int offset) {
        List<MeetingDto> list;
        list = meetingDao.meetingList(limit,offset);
        return list;
    }

    @Override
    public int meetingUpdate(MeetingDto dto) {
        return meetingDao.meetingUpdate(dto);
    }

    @Override
    @Transactional
    public int meetingDelete(int meetingId) {
        int resultParticipants = participantDao.deleteAllParticipant(meetingId);
        int resulMeetingDelete =meetingDao.meetingDelete(meetingId);
        System.out.println(resultParticipants+ " " + resulMeetingDelete);
        if(resulMeetingDelete!=-1 && resultParticipants!=-1){
            return 1;
        }
        return -1;
    }

    @Override
    public List<MeetingDto> myMeetingList(int userId) {
        return null;
    }

    @Override
    public String meetingWritingImageUpload(MultipartFile image) throws IOException {
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
        return amazonS3Client.getUrl(bucket, newFilename).toString();
    }

    @Override
    public String meetingWritingImageDelete(String imageName) {
        String result = "";
        if(("".equals(imageName) || "default".equals(imageName) ||imageName == null)){
            return "잘못된 이미지 이름입니다.";
        }

        try {
            boolean isImageExist = amazonS3Client.doesObjectExist(bucket, imageName);
            if(isImageExist) {
                amazonS3Client.deleteObject(bucket, imageName);
                result = "success";
            }
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }

    @Override
    public List<MeetingDto> specificUserMeetingList(int userId) {
        return meetingDao.specificUserMeetingList(userId);
    }

    @Override
    public int deleteMeetingImage(String imageUrl) {
        int result = -1;

        if(("".equals(imageUrl) || "default".equals(imageUrl) ||imageUrl == null)){
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

    @Override
    public List<MeetingDto> meetingSearchList(int limit, int offset, String searchTitle,
                                              String searchAddr, String meetingStartDate,
                                              String meetingEndDate, int maxPeople,
                                              String meetingPassword) {
        boolean meetingPw = "true".equals(meetingPassword) == true ? true: false;
        return meetingDao.meetingSearchList(limit,offset,searchTitle,searchAddr,meetingStartDate,meetingEndDate,maxPeople,meetingPw);
    }
}
