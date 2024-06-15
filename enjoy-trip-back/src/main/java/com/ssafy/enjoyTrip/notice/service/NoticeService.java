package com.ssafy.enjoyTrip.notice.service;

import com.ssafy.enjoyTrip.notice.dto.NoticeDto;
import com.ssafy.enjoyTrip.user.dto.UserDto;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface NoticeService {

    public int noticeInsert(NoticeDto dto);
    public int noticeUpdate(NoticeDto dto);
    public NoticeDto noticeDetail(int noticeId, UserDto userDto);
    public int noticeDelete(int noticeId);
    public List<NoticeDto> noticeList( int limit, int offset);
    public int noticeListTotalCnt();
    public int noticeListSearchWordTotalCnt(String searchWord);
    public List<NoticeDto> noticeListSearchWord(String searchWord ,int limit, int offset);
    public List<NoticeDto> noticeListByNoticeId(String noticeId ,int limit, int offset);
    public List<NoticeDto> noticeListByUserName(String userName ,int limit, int offset);
    public int noticeListUserNameTotalCnt(String userId);
    public int hit(int noticeId);
}
