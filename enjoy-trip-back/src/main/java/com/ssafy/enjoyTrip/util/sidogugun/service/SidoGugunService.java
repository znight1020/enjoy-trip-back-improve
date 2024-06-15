package com.ssafy.enjoyTrip.util.sidogugun.service;

import java.util.List;

import com.ssafy.enjoyTrip.util.sidogugun.dto.GugunCodeDto;
import com.ssafy.enjoyTrip.util.sidogugun.dto.SidoCodeDto;

public interface SidoGugunService {
	List<SidoCodeDto> sidoCodeList();
	List<GugunCodeDto> gugunCodeList(String sidoCode);
}
