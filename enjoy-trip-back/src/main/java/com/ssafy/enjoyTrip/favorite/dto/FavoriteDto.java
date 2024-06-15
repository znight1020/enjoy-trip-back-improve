package com.ssafy.enjoyTrip.favorite.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FavoriteDto {
	private int favoritesId;
	private int userId;	
	private int attractionId;
	private String attractionName;
}
