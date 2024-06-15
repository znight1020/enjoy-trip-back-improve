package com.ssafy.enjoyTrip.meeting.dao;

import com.ssafy.enjoyTrip.meeting.dto.MeetingDto;
import com.ssafy.enjoyTrip.meeting.dto.ParticipantDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ParticipantDao {
    public int participantInsert(ParticipantDto dto);
    public int isParticipant(@Param("userId")int userId, @Param("meetingId")int meetingId);
    public String getAuthority(@Param("userId")int userId, @Param("meetingId")int meetingId);
    public List<MeetingDto> getParticipatingMeetings(int userId);
    public List<ParticipantDto> joinedParticipants(int meetingId);
    public int deleteParticipant(@Param("userId")int userId, @Param("meetingId")int meetingId);
    public int deleteAllParticipant(int meetingId);
}
