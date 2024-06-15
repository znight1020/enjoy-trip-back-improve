package com.ssafy.enjoyTrip.community.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssafy.enjoyTrip.SessionConst;
import com.ssafy.enjoyTrip.community.dto.CommunityDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssafy.enjoyTrip.community.service.CommunityService;
import com.ssafy.enjoyTrip.user.dto.UserDto;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
@Tag(name = "07. 커뮤니티 컨트롤러 페이지", description = "커뮤니티 관련 api")
public class CommunityController {
	private final CommunityService communityService;

	@GetMapping
	@Operation(summary = "커뮤니티 글 목록을 조회합니다.", description = "limit, offset, 검색어를 파라미터로 받아 커뮤니티 글을 검색하여 반환해주는 기능입니다.")
	public ResponseEntity<Map<String, Object>> communityList(
			@RequestParam("limit") int limit,
			@RequestParam("offset") int offset,
			@RequestParam(value = "searchWord", required = false) String searchWord) {

		List<CommunityDto> communityList;

		if (searchWord == null || "".equals(searchWord)) {
			communityList = communityService.communityList(limit, offset);
		} else {
			communityList = communityService.communityListSearchWord(limit, offset, searchWord);
		}
		
		if (!communityList.isEmpty()) {
			return new ResponseEntity<>(Map.of("result", "success", "communityList", communityList), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/communityListTop")
	@Operation(summary = "상단 커뮤니티 글 목록을 조회합니다.", description = "limit만큼의 상단 게시글 목록을 조회하는 기능입니다.")
	public ResponseEntity<Map<String, Object>> communityListTop(@RequestParam("limit") int limit) {
		List<CommunityDto> communityList = communityService.communityListTop(limit);
		if (!communityList.isEmpty()) {
			return new ResponseEntity<>(Map.of("result", "success", "communityList", communityList), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/communityListTotalCnt")
	@Operation(summary = "커뮤니티 글 전체 개수를 조회합니다.", description = "검색어를 파라미터로 받아 해당 커뮤니티 글의 개수를 반환해주는 기능입니다.")
	public ResponseEntity<Map<String, Object>> communityListTotalCnt(@RequestParam(value = "searchWord", required = false) String searchWord) {
		int totalCnt;
		if (searchWord == null || "".equals(searchWord)) {
			totalCnt = communityService.communityListTotalCnt();
		} else {
			totalCnt = communityService.communityListSearchWordTotalCnt(searchWord);
		}

		if (totalCnt >= 0) {
			return new ResponseEntity<>(Map.of("result", "success", "totalcnt", totalCnt), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	@Operation(summary = "커뮤니티 글을 등록합니다.", description = "커뮤니티 글을 등록하는 기능입니다.")
	public ResponseEntity<Map<String, Object>> communityInsert(@RequestBody CommunityDto communityDto) {
		int ret = communityService.communityInsert(communityDto);
		if (ret == 1) {
			return new ResponseEntity<>(Map.of("result", "success"), HttpStatus.OK);
		} else if (ret == -1) {
			return new ResponseEntity<>(Map.of("result", "login"), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{communityId}")
	@Operation(summary = "커뮤니티 글을 수정합니다.", description = "커뮤니티 글을 수정하는 기능입니다.")
	public ResponseEntity<Map<String, Object>> communityUpdate(@RequestBody CommunityDto communityDto) {
		int ret = communityService.communityUpdate(communityDto);
		if (ret == 1) {
			return new ResponseEntity<>(Map.of("result", "success"), HttpStatus.OK);
		}
		return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@DeleteMapping("/{communityId}")
	@Operation(summary = "커뮤니티 글을 삭제합니다.", description = "커뮤니티 글을 삭제하는 기능입니다.")
	public ResponseEntity<Map<String, Object>> communityDelete(@PathVariable("communityId") int communityId) {
		int ret = communityService.communityDelete(communityId);
		if (ret == 1) {
			return new ResponseEntity<>(Map.of("result", "success"), HttpStatus.OK);
		}
		return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/{communityId}")
	@Operation(summary = "커뮤니티 글을 상세 조회합니다.", description = "커뮤니티 글을 상세 조회하는 기능입니다.")
	public ResponseEntity<Map<String, Object>> communityDetail(@PathVariable("communityId") int communityId, HttpSession session){
		UserDto userDto = (UserDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
		CommunityDto communityDto = communityService.communityDetail(communityId, userDto.getUserId());

		if(communityDto != null) {
			return new ResponseEntity<>(Map.of("result", "success", "community", communityDto), HttpStatus.OK);
		}
		return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping(value = "/uploadImage", consumes = { "multipart/form-data"})
	public ResponseEntity<Map<String, String>> uploadImage(@RequestPart("image") MultipartFile image) throws IOException {
		String imageUrl = communityService.uploadImage(image);
		if(!"".equals(imageUrl)) {
			return new ResponseEntity<>(Map.of("result", "success", "imageUrl", imageUrl), HttpStatus.OK);
		}
		return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping(value = "/deleteImage/{imageName}")
	public ResponseEntity<Map<String, String>> deleteImage(@PathVariable String imageName) {
		int ret = communityService.deleteImage(imageName);
		if(ret == 1) {
			return new ResponseEntity<>(Map.of("result", "success"), HttpStatus.OK);
		} else if (ret == -1) {
			return new ResponseEntity<>(Map.of("result", "fail", "message", "aws 서버 오류"), HttpStatus.BAD_GATEWAY);
		}
		return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PutMapping(value = "/hit/{communityId}")
	public ResponseEntity<Map<String, String>> hitCommunity(@PathVariable int communityId) {
		int ret = communityService.hit(communityId);
		if(ret == 1) {
			return new ResponseEntity<>(Map.of("result", "success"), HttpStatus.OK);
		}
		return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@GetMapping(value = "/write/{userId}")
	public ResponseEntity<Map<String, Object>> specificUserWriteCommunity(@PathVariable("userId") int userId) {
		List<CommunityDto> list = communityService.specificUserWriteCommunity(userId);

		if (!list.isEmpty()) {
			return new ResponseEntity<>(Map.of("result", "success", "communityList", list), HttpStatus.OK);
		}
		return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/delete-image/{imageUrl}")
	public ResponseEntity<Map<String,String>> deleteMeetingImage(@PathVariable("imageUrl") String imageUrl){
		int result = communityService.deleteImage(imageUrl);

		if(result == 1) {
			return new ResponseEntity<>(Map.of("result", "success"), HttpStatus.OK);
		}
		return new ResponseEntity<>(Map.of("result", "fail"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
