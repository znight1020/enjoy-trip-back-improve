package com.ssafy.enjoyTrip.travel.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.enjoyTrip.travel.dto.TravelDto;

@Mapper
public interface TravelDao {
	public List<TravelDto> selectTravelList(@Param("sidoCode") int sidoCode, @Param("gugunCode") int gugunCode);
	public List<TravelDto> selectTravelListWithContent(@Param("sidoCode") int sidoCode,@Param("gugunCode") int gugunCode,@Param("contentType") List contentType);
	public List<TravelDto> selectTravelListWithKeyword(String keyword);

	List<TravelDto> travelListTop();
}
