package com.kh.osori.fixedtrans.service;

import java.util.ArrayList;

import com.kh.osori.fixedtrans.model.dto.request.FixedTransCreateRequest;
import com.kh.osori.fixedtrans.model.dto.request.FixedTransUpdateRequest;
import com.kh.osori.fixedtrans.model.vo.FixedTrans;

public interface FixedTransService {
	
	//클라이언트에서 받아온 데이터를 DB에 넣는 메소드 
	int create(FixedTransCreateRequest req);
	
	//userId로 고정 지출 내역 조회해오기 
	ArrayList<FixedTrans> getFixedList(int userId);
	
	//고정지출 ID를 기반으로 내역 삭제하기 
	int deleteFixedExpense(int fixedId);
	
	//고정 지출 수정하기
	int updateFixedExpense(FixedTransUpdateRequest req); 
	

}
