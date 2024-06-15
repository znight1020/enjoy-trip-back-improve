package com.ssafy.enjoyTrip.auth.dto;

import lombok.Data;

@Data
public class LoginResultDto {
	String result;
	String message;
	LoginDto loginDto;
}
