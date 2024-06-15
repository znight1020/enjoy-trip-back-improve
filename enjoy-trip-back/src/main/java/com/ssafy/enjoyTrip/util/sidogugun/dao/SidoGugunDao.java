package com.ssafy.enjoyTrip.util.sidogugun.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.enjoyTrip.util.sidogugun.dto.GugunCodeDto;
import com.ssafy.enjoyTrip.util.sidogugun.dto.SidoCodeDto;

@Mapper
public interface SidoGugunDao {
	List<SidoCodeDto> sidoCodeList();
	List<GugunCodeDto> gugunCodeList(String sidoCode);
	
}
