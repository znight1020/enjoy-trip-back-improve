package com.ssafy.enjoyTrip.favorite.controller;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssafy.enjoyTrip.favorite.dto.FavoriteDto;
import com.ssafy.enjoyTrip.favorite.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite")
@Slf4j
@Tag(name = "03. 사용자 즐겨찾기 관련 컨트롤러 페이지", description = "즐겨찾기 관련 api")
public class FavoriteController {
	private final FavoriteService favoriteService;

	@GetMapping("/{userId}")
	@Operation(summary = "사용자 즐겨찾기 조회", description = "사용자의 관심 여행지 조회 기능입니다.")
	public ResponseEntity<Map<String, Object>> favoriteList(@PathVariable("userId") int userId){
		List<FavoriteDto> list = favoriteService.favoriteList(userId);
		if (list != null) {
			return new ResponseEntity<>(Map.of("result", "success", "list", list), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(Map.of("result", "fail", "message", "즐겨찾기 목록 조회에 실패하였습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@PostMapping
	@Operation(summary = "사용자 즐겨찾기 등록", description = "사용자의 관심 여행지를 등록하는 기능입니다.")
	public ResponseEntity<Map<String,String>> favoriteAdd(@RequestBody FavoriteDto favoriteDto){
		int ret = favoriteService.favoriteAdd(favoriteDto);
		if (ret > 0) {
			return new ResponseEntity<>(Map.of("result", "success"), HttpStatus.OK);
		}
		return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@DeleteMapping("/{favoriteId}")
	@Operation(summary = "사용자 즐겨찾기 삭제", description = "사용자의 관심 여행지 삭제 기능입니다.")
	public ResponseEntity<Map<String,String>> favoriteDelete(@PathVariable("favoriteId") int favoriteId){
		int ret = favoriteService.favoriteDelete(favoriteId);
		if (ret > 0) {
			return new ResponseEntity<>(Map.of("result", "success"), HttpStatus.OK);
		}
		return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
