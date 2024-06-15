package com.ssafy.enjoyTrip.meeting.controller;

import com.ssafy.enjoyTrip.SessionConst;
import com.ssafy.enjoyTrip.meeting.dto.MeetingDetailDto;
import com.ssafy.enjoyTrip.meeting.dto.MeetingDto;
import com.ssafy.enjoyTrip.meeting.service.MeetingService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssafy.enjoyTrip.user.dto.UserDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/meeting")
@RequiredArgsConstructor
public class MeetingController {
    /*
        public int meetingInsert(MeetingDto dto);
    public MeetingDto meetingSelect(int meetingId);
    public List<MeetingDto> meetingList(int limit, int offset, String searchWord);
    public int meetingUpdate(MeetingDto dto);
    public int meetingDelete(int meetingId);
    public List<MeetingDto> myMeetingList(int userId);
     */
    private final MeetingService meetingService;

    @GetMapping("/posts")
    public ResponseEntity<Map<String,Object>> meetingList(@RequestParam("limit") int limit,
                                                          @RequestParam("offset") int offset){
        Map<String,Object> map = new HashMap<>();
        List<MeetingDto> list=meetingService.meetingList(limit,offset);
        if(!list.isEmpty()){
            map.put("list",list);
            map.put("result", "success");
            System.out.println(list);
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
        }else{
            map.put("result","fail");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
    @GetMapping("/posts/search")
    public ResponseEntity<Map<String,Object>> meetingSearchList(@RequestParam("limit") int limit,
                                                                @RequestParam("offset") int offset,
                                                                @RequestParam("searchTitle") String searchTitle,
                                                                @RequestParam("searchAddr") String searchAddr,
                                                                @RequestParam(name = "meetingStartDate",defaultValue = "2000-01-01") String meetingStartDate,
                                                                @RequestParam(name= "meetingEndDate" ,defaultValue="3000-01-01") String meetingEndDate,
                                                                @RequestParam(name = "maxPeople", defaultValue = "0") int maxPeople,
                                                                @RequestParam("meetingPassword") String meetingPassword) {
        Map<String, Object> map = new HashMap<>();
        System.out.printf("%d %d %s %s %s %s %s %s",limit,offset,searchTitle,searchAddr,meetingStartDate,meetingEndDate,maxPeople, meetingPassword);
        System.out.println(meetingEndDate+" meetingEndTime");
        List<MeetingDto> list = meetingService.meetingSearchList(limit, offset, searchTitle, searchAddr, meetingStartDate, meetingEndDate, maxPeople, meetingPassword);
        if (!list.isEmpty()) {
            map.put("list", list);
            map.put("result", "success");
            System.out.println(list);
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
        } else {
            map.put("result", "fail");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping("/posts")
    public ResponseEntity<Map<String,Object>> meetingInsert(@RequestBody MeetingDto dto){
        log.info("dto={}", dto);
        Map<String,Object> map = new HashMap<>();
        int result = meetingService.meetingInsert(dto);
        System.out.println("result: "+result);
        System.out.println("dto: "+ dto);
        if(result==1){
            map.put("result","success");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
        }else{
            map.put("result","fail");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/posts")
    public ResponseEntity<Map<String,Object>> meetingUpdate(@RequestBody MeetingDto meetingDto){
        Map<String, Object> map = new HashMap<>();
        int ret = meetingService.meetingUpdate(meetingDto);
        if (ret == 1) {
            map.put("result", "success");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
        } else {
            map.put("result", "fail");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/posts/{meetingId}")
    public ResponseEntity<Map<String,Object>> meetingDelete(@PathVariable("meetingId") int meetingId){

        Map<String, Object> map = new HashMap<>();
        int ret = meetingService.meetingDelete(meetingId);
        if (ret == 1) {
            map.put("result", "success");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
        } else {
            map.put("result", "fail");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts/{meetingId}")
    public ResponseEntity<Map<String, Object>> meetingDetail(@PathVariable("meetingId") int meetingId,  HttpSession session){
        Map<String, Object> map = new HashMap<>();
        if(session.getAttribute(SessionConst.LOGIN_MEMBER)==null) {
            UserDto dummy = new UserDto();
            dummy.setUserId(-1);
            MeetingDetailDto meetingDetail = meetingService.meetingDetail(meetingId,dummy);
            map.put("meetingDetail",meetingDetail);
            map.put("result", "success");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
        }
        UserDto userDto = (UserDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        System.out.println(userDto.getUserId());
        MeetingDetailDto meetingDetail = meetingService.meetingDetail(meetingId, userDto);
        map.put("meetingDetail",meetingDetail);
        map.put("result", "success");
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }

    @PostMapping(value = "/writing-image-upload", consumes = { "multipart/form-data"})
    public ResponseEntity<Map<String, String>> meetingWritingImageUpload(@RequestPart("image") MultipartFile image) throws IOException {
        String url = meetingService.meetingWritingImageUpload(image);
        if(!("".equals(url) || url == null)) {
            return new ResponseEntity<>(Map.of("result", "success", "url", url), HttpStatus.OK);
        }
        return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(value = "/writing-image-delete/{imageName}")
    public ResponseEntity<Map<String, String>> meetingWritingImageDelete(@PathVariable("imageName") String imageName) throws IOException {
        String result = meetingService.meetingWritingImageDelete(imageName);
        if("success".equals(result)) {
            return new ResponseEntity<>(Map.of("result", result), HttpStatus.OK);
        }
        return new ResponseEntity<>(Map.of("result", result), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/write/{userId}")
    public ResponseEntity<Map<String,Object>> specificUserMeetingList(@PathVariable("userId") int userId){
        List<MeetingDto> list = meetingService.specificUserMeetingList(userId);
        if(!list.isEmpty()){
            return new ResponseEntity<>(Map.of("meetingList", list, "result", "success"), HttpStatus.OK);
        }
        return new ResponseEntity<>(Map.of("result","fail"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/delete-image/{imageUrl}")
    public ResponseEntity<Map<String,String>> deleteMeetingImage(@PathVariable("imageUrl") String imageUrl){
        int result = meetingService.deleteMeetingImage(imageUrl);

        if(result == 1) {
            return new ResponseEntity<>(Map.of("result", "success"), HttpStatus.OK);
        }
        return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
