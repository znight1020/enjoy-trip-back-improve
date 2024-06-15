package com.ssafy.enjoyTrip.notice.controller;

import com.ssafy.enjoyTrip.SessionConst;
import com.ssafy.enjoyTrip.notice.dto.NoticeDto;
import com.ssafy.enjoyTrip.notice.service.NoticeService;
import com.ssafy.enjoyTrip.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
@Tag(name=" 공지사항 컨트롤러 페이지", description = "공지사항 관련 api")
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("/boards")
    @Operation(summary="공지사항 목록을 조회합니다.", description ="limit, offset, 검색어를 파라미터로 받아 게시글을 검색하여 반환해주는 기능입니다.")
    public ResponseEntity<Map<String, Object>> noticeList(@RequestParam("limit") int limit,
                                                          @RequestParam("offset") int offset,
                                                          @RequestParam("searchWord") String searchWord,
                                                          @RequestParam("searchOption") String searchOption){
        List<NoticeDto> noticeList = getFilteredNoticeList(limit, offset, searchWord, searchOption);
        if(!noticeList.isEmpty()){
            return new ResponseEntity<>(Map.of("noticeList", noticeList, "result", "success"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/noticeListTotalCnt")
    @Operation(summary = "공지사항 전체 개수를 조회합니다." , description = "검색어를 파라미터로 받아 해당 공지사항의 개수를 반환해주는 기능입니다.")
    public ResponseEntity<Map<String, Object>> noticeListTotalCnt(@RequestParam("searchWord") String searchWord, @RequestParam("searchOption")String searchOption) {
        int totalCount = getNoticeTotalCount(searchWord, searchOption);

        if (totalCount >= 0) {
            return new ResponseEntity<>(Map.of("totalCnt", totalCount, "result", "success"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/boards")
    @Operation(summary = "공지사항을 등록합니다.", description = "공지사항 등록하는 기능입니다.")
    public ResponseEntity<Map<String, Object>> noticeInsert(@RequestBody NoticeDto noticeDto, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute(SessionConst.LOGIN_MEMBER);

        noticeDto.setUserId(user.getUserId());

        int ret = noticeService.noticeInsert(noticeDto);
        if (ret == 1) {
            return new ResponseEntity<>(Map.of("result", "success"), HttpStatus.OK);
        } else if (ret == -1) {
            return new ResponseEntity<>(Map.of("result", "login"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/boards")
    @Operation(summary = "게시글을 수정합니다.", description = "게시글을 수정하는 기능입니다.")
    public ResponseEntity<Map<String, Object>> noticeUpdate(@RequestBody NoticeDto noticeDto) {
        int ret = noticeService.noticeUpdate(noticeDto);
        if (ret == 1) {
            return new ResponseEntity<>(Map.of("result", "success"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/boards/{noticeId}")
    @Operation(summary = "게시글을 삭제합니다.", description = "게시글을 삭제하는 기능입니다.")
    public ResponseEntity<Map<String, Object>> noticeDelete(@PathVariable("noticeId") int noticeId) {
        int ret = noticeService.noticeDelete(noticeId);
        if (ret == 1) {
            return new ResponseEntity<>(Map.of("result", "success"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/boards/{noticeId}")
    @Operation(summary = "게시글을 상세 조회합니다.", description = "게시글을 상세 조회하는 기능입니다.")
    public ResponseEntity<Map<String, Object>> noticeDetail(@PathVariable("noticeId") int noticeId, HttpSession session){
        //postman에서는 session 존재하지 않기 때문에 error 발생함. dummy data로 test 권장
        //로그인 하지 않은 사용자는 글조회는 할 수 있되, admin, sameUser를 모두 false로 두어서 글수정,글삭제에 대한 권한 없도록..
        if(session.getAttribute(SessionConst.LOGIN_MEMBER)==null){
            UserDto dummy = new UserDto();
            dummy.setUserId(-1);

            NoticeDto noticeDto = noticeService.noticeDetail(noticeId, dummy);
            return new ResponseEntity<>(Map.of("noticeDto", noticeDto, "result", "success"), HttpStatus.OK);
        }

        UserDto userDto = (UserDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        NoticeDto noticeDto = noticeService.noticeDetail(noticeId, userDto);
        return new ResponseEntity<>(Map.of("noticeDto", noticeDto, "result", "success"), HttpStatus.OK);
    }

    @PutMapping(value = "/hit/{noticeId}")
    public ResponseEntity<Map<String, String>> hitNotice(@PathVariable int noticeId) {
        int ret = noticeService.hit(noticeId);
        if(ret == 1) {
            return new ResponseEntity<>(Map.of("result", "success"), HttpStatus.OK);
        }
        return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private List<NoticeDto> getFilteredNoticeList(int limit, int offset, String searchWord, String searchOption) {
        List<NoticeDto> noticeList;
        if ("".equals(searchWord)) {
            noticeList = noticeService.noticeList(limit, offset);
        } else {
            //글번호 noticeId
            if("noticeId".equals(searchOption)){
                noticeList = noticeService.noticeListByNoticeId(searchWord, limit, offset);
            }
            //작성자 userId..이지만 닉네임으로 검색
            else if("userId".equals(searchOption)){
                noticeList = noticeService.noticeListByUserName(searchWord, limit, offset);
            }
            //제목 title
            else if("title".equals(searchOption)){
                noticeList = noticeService.noticeListSearchWord(searchWord, limit, offset);
            }
            //아무 조건에도 속하지 않으면 그냥 전체에 대해서 검색
            else {
                noticeList = noticeService.noticeList(limit, offset);
            }
        }
        return noticeList;
    }

    private int getNoticeTotalCount(String searchWord, String searchOption) {
        int totalCnt;
        if ("".equals(searchWord)) {
            totalCnt = noticeService.noticeListTotalCnt();
        } else if("userId".equals(searchOption)){
            totalCnt = noticeService.noticeListUserNameTotalCnt(searchWord);
        } else {
            totalCnt = noticeService.noticeListSearchWordTotalCnt(searchWord);
        }
        return totalCnt;
    }
}