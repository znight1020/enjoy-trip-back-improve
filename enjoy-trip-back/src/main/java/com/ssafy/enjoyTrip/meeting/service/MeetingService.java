package com.ssafy.enjoyTrip.meeting.service;

import com.ssafy.enjoyTrip.meeting.dto.MeetingDetailDto;
import com.ssafy.enjoyTrip.meeting.dto.MeetingDto;
import com.ssafy.enjoyTrip.user.dto.UserDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface MeetingService {
    public int meetingInsert(MeetingDto dto);
    public MeetingDetailDto meetingDetail(int meetingId, UserDto dto);
    public List<MeetingDto> meetingList(int limit, int offset);
    public int meetingUpdate(MeetingDto dto);
    public int meetingDelete(int meetingId);
    public List<MeetingDto> myMeetingList(int userId);

    public String meetingWritingImageUpload(MultipartFile image) throws IOException;
    public String meetingWritingImageDelete(String imageName) throws IOException;
    List<MeetingDto> specificUserMeetingList(int userId);
    int deleteMeetingImage(String imageUrl);
    public List<MeetingDto>  meetingSearchList(int limit,
                                              int offset,
                                               String searchTitle,
                                               String searchAddr,
                                               String meetingStartDate,
                                               String meetingEndDate,
                                               int maxPeople,
                                               String meetingPassword );
}
