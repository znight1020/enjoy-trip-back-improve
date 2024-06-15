package com.ssafy.enjoyTrip.meeting.controller;

import com.ssafy.enjoyTrip.meeting.dto.ParticipantDto;
import com.ssafy.enjoyTrip.meeting.service.ParticipantService;

import java.util.Map;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ParticipantController {
    private final ParticipantService participantService;
    //INSERT 소모임 가입
    @PostMapping("/participants")
    public ResponseEntity<Map<String,Object>> participantInsert(@RequestBody ParticipantDto dto){
        int result = participantService.participantInsert(dto);
        if(result==1){
            return new ResponseEntity<>(Map.of("result","success"), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(Map.of("result","fail"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //DELETE 소모임 탈퇴
    @DeleteMapping("/participants/{userId}/{meetingId}")
    public ResponseEntity<Map<String,Object>> participantsDelete(@PathVariable("userId") int userId,@PathVariable("meetingId") int meetingId){
        int ret = participantService.deleteParticipant(userId,meetingId);
        if (ret == 1) {
            return new ResponseEntity<>(Map.of("result","success"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("result","fail"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //SELECT 내가 속한 소모임
}
