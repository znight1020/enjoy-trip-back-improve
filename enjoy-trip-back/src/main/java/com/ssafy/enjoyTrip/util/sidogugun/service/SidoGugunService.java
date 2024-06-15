package com.ssafy.enjoyTrip.util.sidogugun.service;

import java.util.List;

import com.ssafy.enjoyTrip.util.sidogugun.dto.GugunCodeDto;
import com.ssafy.enjoyTrip.util.sidogugun.dto.SidoCodeDto;

public interface SidoGugunService {
	public List<SidoCodeDto> sidoCodeList();
	public List<GugunCodeDto> gugunCodeList(String sidoCode);
}
