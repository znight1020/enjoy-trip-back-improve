package com.ssafy.enjoyTrip.travel.service;

import java.util.List;

import com.ssafy.enjoyTrip.community.dto.CommunityDto;
import com.ssafy.enjoyTrip.travel.dto.TravelDto;

public interface TravelService {
	public List<TravelDto> selectTravelList(int sidoCode, int gugunCode);
	public List<TravelDto> selectTravelListWithContent(int sidoCode, int gugunCode, String contentType);
	public List<TravelDto> selectTravelListWithKeyword(String keyword);
	List<TravelDto> travelListTop();
}
