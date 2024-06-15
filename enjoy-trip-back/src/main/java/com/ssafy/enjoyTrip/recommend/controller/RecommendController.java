package com.ssafy.enjoyTrip.recommend.controller;

import com.ssafy.enjoyTrip.recommend.dto.RecommendDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.enjoyTrip.recommend.service.RecommendService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommend")
@Tag(name = "06. 추천 관련 컨트롤러 페이지", description = "추천 관련 api")
public class RecommendController {

	private final RecommendService recommendService;
	
	@GetMapping("/{userId}")
	@Operation(summary = "사용자 기반 여행지 추천", description = "사용자 기반에 따른 여행지를 추천하는 기능입니다.")
	public ResponseEntity<Map<String,Object>> recommendWithAddr(@PathVariable int userId){
		List<RecommendDto> recommendList= recommendService.recommendWithAddr(userId);
		
		if (recommendList != null) {
			return new ResponseEntity<>(Map.of("result", "success", "list", recommendList), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(Map.of("result", "fail"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/region/{userId}")
	@Operation(summary = "사용자 지역 기반 여행지 추천", description = "사용자가 입력한 지역 기반에 따른 여행지를 추천하는 기능입니다.")
	public ResponseEntity<Map<String,Object>> recommendPopular(@PathVariable int userId){
		List<RecommendDto> recommendList = recommendService.recommendPopular(userId);
		
		if (recommendList != null) {
			return new ResponseEntity<>(Map.of("result", "success", "list", recommendList), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(Map.of("result", "fail"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/type/{userId}")
	@Operation(summary = "사용자 콘텐츠 기반 여행지 추천", description = "사용자가 선호하는 콘텐츠 기반에 따른 여행지를 추천하는 기능입니다.")
	public ResponseEntity<Map<String,Object>> recommendType(@PathVariable int userId){
		List<RecommendDto> recommendDto = recommendService.recommendByType(userId);
		
		if (recommendDto != null) {
			return new ResponseEntity<>(Map.of("result", "success", "recommendDto", recommendDto), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(Map.of("result", "fail"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
