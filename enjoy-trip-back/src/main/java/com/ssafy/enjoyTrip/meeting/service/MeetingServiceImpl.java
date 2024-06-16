package com.ssafy.enjoyTrip.meeting.service;

import com.ssafy.enjoyTrip.meeting.dao.MeetingDao;
import com.ssafy.enjoyTrip.meeting.dao.ParticipantDao;
import com.ssafy.enjoyTrip.meeting.dto.MeetingDetailDto;
import com.ssafy.enjoyTrip.meeting.dto.MeetingDto;

import java.io.IOException;
import java.util.List;

import com.ssafy.enjoyTrip.meeting.dto.ParticipantDto;
import com.ssafy.enjoyTrip.user.dto.UserDto;
import com.ssafy.enjoyTrip.util.image.service.ImageService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService{
    private final ImageService imageService;

    private final MeetingDao meetingDao;

    private final ParticipantDao participantDao;

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
        checkUserAuthorityForUpdateAndDelete(dto, meetingDto, detailDto, participantList);

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
    public String meetingWritingImageUpload(MultipartFile multipartFile) throws IOException {
        return imageService.imageUpload("meeting", multipartFile);
    }

    @Override
    public Integer meetingWritingImageDelete(String fileName) {
        return imageService.imageDelete("meeting", fileName);
    }

    @Override
    public List<MeetingDto> specificUserMeetingList(int userId) {
        return meetingDao.specificUserMeetingList(userId);
    }

    @Override
    public List<MeetingDto> meetingSearchList(int limit, int offset, String searchTitle,
                                              String searchAddr, String meetingStartDate,
                                              String meetingEndDate, int maxPeople,
                                              String meetingPassword) {
        boolean meetingPw = "true".equals(meetingPassword);
        return meetingDao.meetingSearchList(limit,offset,searchTitle,searchAddr,meetingStartDate,meetingEndDate,maxPeople,meetingPw);
    }

    private static void checkUserAuthorityForUpdateAndDelete(UserDto dto, MeetingDto meetingDto, MeetingDetailDto detailDto, List<ParticipantDto> participantList) {
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
    }
}
