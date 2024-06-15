package com.ssafy.enjoyTrip.recommend.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.enjoyTrip.recommend.dto.RecommendDto;

@Mapper
public interface RecommendDao {
	List<RecommendDto> recommendWithAddr(int userId);
	List<RecommendDto> recommendPopular();
	List<RecommendDto> recommendByType(int userId);
	List<RecommendDto> recommendFavoriteAddr(int userId);
}
