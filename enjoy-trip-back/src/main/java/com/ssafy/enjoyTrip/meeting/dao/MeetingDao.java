package com.ssafy.enjoyTrip.meeting.dao;

import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.ssafy.enjoyTrip.meeting.dto.MeetingDto;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MeetingDao {
	public int meetingInsert(MeetingDto dto);
	public MeetingDto meetingDetail(int meetingId);
	public List<MeetingDto> meetingList(@Param("limit") int limit,
										@Param("offset") int offset);

	public List<MeetingDto> meetingSearchList(@Param("limit") int limit,
										@Param("offset") int offset,
										@Param("searchTitle") String searchTitle,
										@Param("searchAddr") String searchAddr,
										@Param("meetingStartDate") String meetingStartDate,
										@Param("meetingEndDate") String meetingEndDate,
										@Param("maxPeople") int maxPeople,
										@Param("meetingPassword") boolean meetingPassword );

	public int meetingUpdate(MeetingDto dto);
	public int meetingDelete(int meetingId);
	public List<MeetingDto> myMeetingList(int userId);
	List<MeetingDto> specificUserMeetingList(int userId);
}

