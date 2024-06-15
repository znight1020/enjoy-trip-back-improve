package com.ssafy.enjoyTrip.travel.dto;

import lombok.Data;

@Data
public class TravelDto {
	private int attractionId;
	private String title;
	private String addr1;
	private String addr2;
	private double latitude;
	private double longitude;
	private String firstImage;
	private String firstImage2;
}
