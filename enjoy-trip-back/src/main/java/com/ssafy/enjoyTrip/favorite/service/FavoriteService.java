package com.ssafy.enjoyTrip.favorite.service;

import java.util.List;

import com.ssafy.enjoyTrip.favorite.dto.FavoriteDto;

public interface FavoriteService {
	List<FavoriteDto> favoriteList(int userId);
	int favoriteAdd(FavoriteDto favoriteDto);
	int favoriteDelete(int favoriteId);
}
