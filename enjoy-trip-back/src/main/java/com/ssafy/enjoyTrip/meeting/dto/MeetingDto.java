package com.ssafy.enjoyTrip.meeting.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MeetingDto {
	int meetingId;
	int userId;
	String title;
	String content;
	String meetingPassword;
	String userName;
	String addr1;
	String firstImage;
	String firstImage2;
	String userProfileImageUrl;
	int attractionId;
	private double latitude;
	private double longitude;
	LocalDateTime meetingStartDate;
	LocalDateTime meetingEndDate;
	String thumbnailUrl;
	LocalDateTime regDate;
	boolean sameUser;
	boolean admin;
	int maxPeople;
}
