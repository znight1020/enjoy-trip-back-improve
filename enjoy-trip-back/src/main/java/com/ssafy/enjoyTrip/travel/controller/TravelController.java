package com.ssafy.enjoyTrip.travel.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.enjoyTrip.travel.dto.TravelDto;
import com.ssafy.enjoyTrip.travel.service.TravelService;

import lombok.RequiredArgsConstructor;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/travel")
@Tag(name = "05. 관광지 상세 검색 컨트롤러 페이지", description = "관광지 검색 관련 api")
public class TravelController {
	private final TravelService travelService;
	
	@GetMapping("/content")
	@Operation(summary = "시,도,구,군 + 컨텐츠 관광지 검색", description = "사용자가 선택한 시, 도, 구, 군과 컨텐츠에 따른 관광지를 조회하는 기능입니다.")
	public ResponseEntity<Map<String,Object>> travelListByContent(
			@RequestParam("sidoCode") int sidoCode,
			@RequestParam("gugunCode") int gugunCode,
			@RequestParam("contentType") String contentType
			){
		List<TravelDto> list =travelService.selectTravelListWithContent(sidoCode, gugunCode, contentType);
		Map<String, Object> map = new HashMap<>();
		if(list.size()!=0) {
			map.put("list",list);
			map.put("result", "success");	
			return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		}else {
			map.put("result","fail");
			return new ResponseEntity<Map<String,Object>>(map,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("list")
	@Operation(summary = "시,도,구,군 관광지 검색", description = "사용자가 선택한 시, 도, 구, 군에 따른 관광지를 조회하는 기능입니다.")
	public ResponseEntity<Map<String,Object>> travelList(
			@RequestParam("sidoCode") int sidoCode,
			@RequestParam("gugunCode") int gugunCode
			){
		List<TravelDto> list =travelService.selectTravelList(sidoCode, gugunCode);
		Map<String, Object> map = new HashMap<>();
		if(list.size()!=0) {
			map.put("list",list);
			map.put("result", "success");	
			return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		}else {
			map.put("result","fail");
			return new ResponseEntity<Map<String,Object>>(map,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/keyword")
	@Operation(summary = "관광지 지역명 검색 기능", description = "사용자가 입력한 관광지를 조회하는 기능입니다.")
	public ResponseEntity<Map<String,Object>> travelListByKeyword(
			@RequestParam("keyword") String keyword
			){
		List<TravelDto> list =travelService.selectTravelListWithKeyword(keyword);
		if(list.size()!=0) {
			return new ResponseEntity<>(Map.of("list", list, "result", "success"),HttpStatus.OK);
		}
		return new ResponseEntity<>(Map.of("result", "fail"),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/travelListTop")
	@Operation(summary = "인기 관광지 목록을 조회합니다.")
	public ResponseEntity<Map<String, Object>> travelListTop() {
		List<TravelDto> travelList = travelService.travelListTop();
		log.info("list: {}", travelList);
		if (!travelList.isEmpty()) {
			return new ResponseEntity<>(Map.of("result", "success", "travelList", travelList), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
