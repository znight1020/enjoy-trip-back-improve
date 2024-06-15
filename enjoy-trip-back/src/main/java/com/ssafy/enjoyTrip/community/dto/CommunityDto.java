package com.ssafy.enjoyTrip.community.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommunityDto {
    private int communityId;
    private String category;
    private int userId;
    private String name;
    private String userProfileImageUrl;
    private String title;
    private String content;
    private LocalDateTime regDt;
    private int readCount;
    
    private boolean sameUser;
}