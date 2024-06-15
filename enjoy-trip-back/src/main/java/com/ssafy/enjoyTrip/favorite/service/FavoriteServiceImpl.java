package com.ssafy.enjoyTrip.favorite.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.enjoyTrip.favorite.dao.FavoriteDao;
import com.ssafy.enjoyTrip.favorite.dto.FavoriteDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService{
	
	private final FavoriteDao favoriteDao;
	
	@Override
	public List<FavoriteDto> favoriteList(int userId) {
		return favoriteDao.favoriteList(userId);
	}

	@Override
	public int favoriteAdd(FavoriteDto favoriteDto) {
		return favoriteDao.favoriteAdd(favoriteDto);
	}

	@Override
	public int favoriteDelete(int favoriteId) {
		return favoriteDao.favoriteDelete(favoriteId);
	}

}
