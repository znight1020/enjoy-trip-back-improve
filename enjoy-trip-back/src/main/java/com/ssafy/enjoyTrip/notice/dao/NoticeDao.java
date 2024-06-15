package com.ssafy.enjoyTrip.notice.dao;


import com.ssafy.enjoyTrip.notice.dto.NoticeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeDao {
    public int noticeInsert(NoticeDto dto);
    public int noticeUpdate(NoticeDto dto);
    public NoticeDto noticeDetail(int noticeId);
    public int noticeDelete(int noticeId);
    public List<NoticeDto> noticeList(@Param("limit") int limit, @Param("offset") int offset);
    public int noticeListTotalCnt();
    public int noticeListSearchWordTotalCnt(String searchWord);
    public int noticeListUserNameTotalCnt(String noticeId);
    public List<NoticeDto> noticeListSearchWord(@Param("searchWord") String searchWord , @Param("limit") int limit, @Param("offset") int offset);
    public List<NoticeDto> noticeListByUserName(@Param("userName") String userName , @Param("limit") int limit, @Param("offset") int offset);
    public List<NoticeDto> noticeListByNoticeId(@Param("noticeId") String noticeId , @Param("limit") int limit, @Param("offset") int offset);
    int hit(int data, int noticeId);
    int readHit(int noticeId);
}
