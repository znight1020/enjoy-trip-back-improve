package com.ssafy.enjoyTrip.favorite.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.enjoyTrip.favorite.dto.FavoriteDto;

@Mapper
public interface FavoriteDao {
	List<FavoriteDto> favoriteList(int userId);
	int favoriteAdd(FavoriteDto favoriteDto);
	int favoriteDelete(int favoriteId);
}
