package com.ssafy.enjoyTrip.user.dto;

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
public class UserDto {
	private int userId;
    private String name;
    private String password;
    private String email;
    private String userProfileImageUrl;
    private String sido;
    private String gugun;
    private String code;
}
