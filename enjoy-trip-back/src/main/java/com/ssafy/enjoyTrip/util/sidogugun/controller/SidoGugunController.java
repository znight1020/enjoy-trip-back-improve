package com.ssafy.enjoyTrip.util.sidogugun.controller;

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

import com.ssafy.enjoyTrip.util.sidogugun.dto.GugunCodeDto;
import com.ssafy.enjoyTrip.util.sidogugun.dto.SidoCodeDto;
import com.ssafy.enjoyTrip.util.sidogugun.service.SidoGugunService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/trip")
@RequiredArgsConstructor
@Tag(name = "04. 관광지 관련 컨트롤러 페이지", description = "관광지 관련 api")
public class SidoGugunController {
	private final SidoGugunService sidoGugunService;
	@GetMapping("/sidoCode")
	@Operation(summary = "시, 도 관광지 조회", description = "사용자가 선택한 시, 도 관광지의 조회 기능입니다.")
	public ResponseEntity<Map<String,Object>> sidoCode(){
		List<SidoCodeDto> list =sidoGugunService.sidoCodeList();
		Map<String, Object> map = new HashMap<>();
		map.put("sidoList", list);

		if(list.size() != 0) {
			map.put("result", "success");	
			return new ResponseEntity<>(map,HttpStatus.OK);
		}else {
			map.put("result","fail");
			return new ResponseEntity<>(map,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/gugunCode/{sidoCode}")
	@Operation(summary = "구, 군 관광지 조회", description = "사용자가 선택한 구, 군 관광지의 조회 기능입니다.")
	public ResponseEntity<Map<String,Object>> gugunCode(@PathVariable("sidoCode") String sidoCode){
		List<GugunCodeDto> list =sidoGugunService.gugunCodeList(sidoCode);
		Map<String, Object> map = new HashMap<>();
		map.put("gugunList", list);

		if(list.size() != 0) {
			map.put("result", "success");	
			return new ResponseEntity<>(map,HttpStatus.OK);
		}else {
			map.put("result","fail");
			return new ResponseEntity<>(map,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
