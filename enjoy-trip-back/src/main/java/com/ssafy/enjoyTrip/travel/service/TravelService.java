package com.ssafy.enjoyTrip.travel.service;

import java.util.List;
import com.ssafy.enjoyTrip.travel.dto.TravelDto;

public interface TravelService {
	List<TravelDto> selectTravelList(int sidoCode, int gugunCode);
	List<TravelDto> selectTravelListWithContent(int sidoCode, int gugunCode, String contentType);
	List<TravelDto> selectTravelListWithKeyword(String keyword);
	List<TravelDto> travelListTop();
}
