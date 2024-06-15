package com.ssafy.enjoyTrip.recommend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RecommendDto {
	
	private int attractionId;
	private int contentTypeId;
	private String title;
	private String addr1;
	private String addr2;
	private String zipcode;
	private String tel;
	private String firstImage;
	private String firstImage2;
	private int readCount;
	private int favoriteCount;
	private int gugunCode;
	private int sidoCode;
	private double latitude;
	private double longitude;
	private String mlevel;
	
}
