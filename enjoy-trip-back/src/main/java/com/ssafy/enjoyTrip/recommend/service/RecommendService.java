package com.ssafy.enjoyTrip.recommend.service;

import com.ssafy.enjoyTrip.recommend.dto.RecommendDto;
import java.util.List;

public interface RecommendService {
	List<RecommendDto> recommendWithAddr(int userId);
	List<RecommendDto> recommendPopular(int userId);
	List<RecommendDto> recommendByType(int userId);
}
