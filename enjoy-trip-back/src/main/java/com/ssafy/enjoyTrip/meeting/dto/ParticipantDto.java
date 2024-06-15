package com.ssafy.enjoyTrip.meeting.dto;

import lombok.Data;

@Data
public class ParticipantDto {
	int meetingId;
	int userId;
	String name;
	String email;
	String userProfileImageUrl;
	String authority;
}
