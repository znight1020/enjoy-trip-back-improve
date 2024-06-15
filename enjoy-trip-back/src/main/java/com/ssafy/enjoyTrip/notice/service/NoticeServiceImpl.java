package com.ssafy.enjoyTrip.notice.service;

import com.ssafy.enjoyTrip.notice.dao.NoticeDao;
import com.ssafy.enjoyTrip.notice.dto.NoticeDto;
import com.ssafy.enjoyTrip.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements  NoticeService{
    private final NoticeDao noticeDao;

    @Override
    public int noticeInsert(NoticeDto dto) {
        return noticeDao.noticeInsert(dto);
    }

    @Override
    public int noticeUpdate(NoticeDto dto) {
        return noticeDao.noticeUpdate(dto);
    }

    @Override
    public NoticeDto noticeDetail(int noticeId, UserDto userDto) {
        NoticeDto noticeDto = noticeDao.noticeDetail(noticeId);
        checkUserAuthorityForUpdateAndDelete(userDto, noticeDto);
        return noticeDto;
    }

    @Override
    public int noticeDelete(int noticeId) {
        return noticeDao.noticeDelete(noticeId);
    }

    @Override
    public List<NoticeDto> noticeList(int limit, int offset) {
        return noticeDao.noticeList(limit,offset);
    }

    @Override
    public int noticeListTotalCnt() {
        return noticeDao.noticeListTotalCnt();
    }

    @Override
    public int noticeListUserNameTotalCnt(String searchWord) {
        return noticeDao.noticeListUserNameTotalCnt(searchWord);
    }

    @Override
    public int noticeListSearchWordTotalCnt(String searchWord) {
        return noticeDao.noticeListSearchWordTotalCnt(searchWord);
    }

    @Override
    public List<NoticeDto> noticeListSearchWord(String searchWord, int limit, int offset) {
        return noticeDao.noticeListSearchWord(searchWord, limit, offset);
    }

    @Override
    public List<NoticeDto> noticeListByNoticeId(String searchWord, int limit, int offset) {
        return  noticeDao.noticeListByNoticeId(searchWord, limit, offset);
    }

    @Override
    public List<NoticeDto> noticeListByUserName(String searchWord, int limit, int offset) {
        return  noticeDao.noticeListByUserName(searchWord, limit, offset);
    }
    @Override
    public int hit(int noticeId) {
        int data = noticeDao.readHit(noticeId) + 1;
        return noticeDao.hit(data, noticeId);
    }

    private static void checkUserAuthorityForUpdateAndDelete(UserDto userDto, NoticeDto noticeDto) {
        if(noticeDto.getUserId() == userDto.getUserId()){
            noticeDto.setSameUser(true);
        } else{
            noticeDto.setSameUser(false);
            noticeDto.setAdmin(false);
        }
        if("1".equals(userDto.getCode())){
            noticeDto.setAdmin(true);
        }
    }
}
