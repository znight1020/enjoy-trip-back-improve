package com.ssafy.enjoyTrip.meeting.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MeetingDetailDto {
	MeetingDto meetingDto;
	List<ParticipantDto> partiList;
}
