package com.ssafy.enjoyTrip.util.sidogugun.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.enjoyTrip.util.sidogugun.dao.SidoGugunDao;
import com.ssafy.enjoyTrip.util.sidogugun.dto.GugunCodeDto;
import com.ssafy.enjoyTrip.util.sidogugun.dto.SidoCodeDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SidoGugunServiceImpl implements SidoGugunService{

	private final SidoGugunDao sidoGugunDao;
	@Override
	public List<SidoCodeDto> sidoCodeList() {
		return sidoGugunDao.sidoCodeList();
	}

	@Override
	public List<GugunCodeDto> gugunCodeList(String sidoCode) {
		return sidoGugunDao.gugunCodeList(sidoCode);
	}
}
