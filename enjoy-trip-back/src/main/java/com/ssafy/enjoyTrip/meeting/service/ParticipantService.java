package com.ssafy.enjoyTrip.meeting.service;

import com.ssafy.enjoyTrip.meeting.dto.MeetingDto;
import com.ssafy.enjoyTrip.meeting.dto.ParticipantDto;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ParticipantService {
    public int participantInsert(ParticipantDto dto);
    public int isParticipant(int userId, int meetingId);
    public String getAuthority(int userId, int meetingId);
    public List<MeetingDto> getParticipatingMeetings(int userId);
    public List<ParticipantDto> joinedParticipants(int meetingId);
    public int deleteParticipant(int userId,int meetingId);
    public int deleteAllParticipant(int meetingId);
}
