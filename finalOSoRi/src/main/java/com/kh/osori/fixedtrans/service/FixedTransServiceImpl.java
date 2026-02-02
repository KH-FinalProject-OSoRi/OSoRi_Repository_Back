package com.kh.osori.fixedtrans.service;

import java.util.ArrayList;


import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.osori.fixedtrans.dao.FixedTransDao;
import com.kh.osori.fixedtrans.model.dto.request.FixedTransCreateRequest;
import com.kh.osori.fixedtrans.model.dto.request.FixedTransUpdateRequest;
import com.kh.osori.fixedtrans.model.vo.FixedTrans;

@Service
public class FixedTransServiceImpl implements FixedTransService {
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Autowired
	private FixedTransDao dao;
	
	@Override
	public int create(FixedTransCreateRequest req) {
		int result = dao.create(sqlSession, req);
		
		return result; 
	}
	
	@Override
	public ArrayList<FixedTrans> getFixedList(int userId) {
		
		ArrayList<FixedTrans> fixedList = dao.getFixedList(sqlSession, userId);
		
		return fixedList;
	}
	
	@Override
	public int deleteFixedExpense(int fixedId) {
		
		int result = dao.deleteFixedExpense(sqlSession, fixedId);
		
		return result; 
		
	}
	
	@Override
	public int updateFixedExpense(FixedTransUpdateRequest req) {
		
		int result = dao.updateFixedExpense(sqlSession, req);
		
		return result; 
		
	}

}
