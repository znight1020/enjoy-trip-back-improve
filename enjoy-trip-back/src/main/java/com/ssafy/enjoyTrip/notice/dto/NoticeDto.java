package com.ssafy.enjoyTrip.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDto {
    int noticeId;
    int userId;
    String title;
    String content;
    int readCount;
    LocalDateTime regDate;
    private String userName;
    private String userProfileImageUrl;
    private boolean sameUser;
    private String code;
    private boolean admin;
}
