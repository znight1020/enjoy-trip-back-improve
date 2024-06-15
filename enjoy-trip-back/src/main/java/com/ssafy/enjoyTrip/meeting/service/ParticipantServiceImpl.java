package com.ssafy.enjoyTrip.meeting.service;

import com.ssafy.enjoyTrip.meeting.dao.ParticipantDao;
import com.ssafy.enjoyTrip.meeting.dto.MeetingDto;
import com.ssafy.enjoyTrip.meeting.dto.ParticipantDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService{

    private final ParticipantDao participantDao;
    @Override
    public int participantInsert(ParticipantDto dto) {
        return participantDao.participantInsert(dto);
    }

    @Override
    public int isParticipant(int userId, int meetingId) {
        return participantDao.isParticipant(userId, meetingId);
    }

    @Override
    public String getAuthority(int userId, int meetingId) {
        return participantDao.getAuthority(userId,meetingId);
    }

    @Override
    public List<MeetingDto> getParticipatingMeetings(int userId) {
        return participantDao.getParticipatingMeetings(userId);
    }

    @Override
    public List<ParticipantDto> joinedParticipants(int meetingId) {
        return participantDao.joinedParticipants(meetingId);
    }

    @Override
    public int deleteParticipant(int userId, int meetingId) {
        return participantDao.deleteParticipant(userId,meetingId);
    }

    @Override
    public int deleteAllParticipant(int meetingId) {
        return participantDao.deleteAllParticipant(meetingId);
    }
}
