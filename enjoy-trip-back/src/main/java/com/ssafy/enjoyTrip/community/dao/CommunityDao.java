package com.ssafy.enjoyTrip.community.dao;

import java.util.List;

import com.ssafy.enjoyTrip.community.dto.CommunityDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommunityDao {
	int communityInsert(CommunityDto dto);
	int communityUpdate(CommunityDto dto);
	int communityDelete(int communityId);
	int communityListTotalCnt();
	CommunityDto communityDetail(int communityId);
	List<CommunityDto> communityList(@Param("limit") int limit, @Param("offset") int offset);
	List<CommunityDto> communityListTop(int limit);
	int communityListSearchWordTotalCnt(String searchWord);
	List<CommunityDto> communityListSearchWord(@Param("limit") int limit, @Param("offset") int offset, @Param("searchWord") String searchWord);
	int hit(int data, int communityId);
	int readHit(int communityId);
	List<CommunityDto> specificUserWriteCommunity(int userId);
}
