package com.ssafy.enjoyTrip.travel.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.enjoyTrip.travel.dao.TravelDao;
import com.ssafy.enjoyTrip.travel.dto.TravelDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelServiceImpl implements TravelService {
	private final TravelDao travelDao;
	@Override
	public List<TravelDto> selectTravelList(int sidoCode, int gugunCode) {
		return travelDao.selectTravelList(sidoCode, gugunCode);
	}

	@Override
	public List<TravelDto> selectTravelListWithContent(int sidoCode, int gugunCode, String contentType) {
		String[] splitContentType =contentType.split("/");
		return travelDao.selectTravelListWithContent(sidoCode, gugunCode, Arrays.stream(splitContentType).toList());
	}

	@Override
	public List<TravelDto> selectTravelListWithKeyword(String keyword) {
		return travelDao.selectTravelListWithKeyword(keyword);
	}

	@Override
	public List<TravelDto> travelListTop() {
		return travelDao.travelListTop();
	}

}
