package com.ssafy.enjoyTrip.recommend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.enjoyTrip.recommend.dao.RecommendDao;
import com.ssafy.enjoyTrip.recommend.dto.RecommendDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {
	
	private final RecommendDao recommendDao;
	
	@Override
	public List<RecommendDto> recommendWithAddr(int userId) {
		return recommendDao.recommendWithAddr(userId);
	}

	@Override
	public List<RecommendDto> recommendPopular(int userId) {
		return recommendDao.recommendPopular();
	}

	@Override
	public List<RecommendDto> recommendByType(int userId) {
		return recommendDao.recommendByType(userId);
	}
}
