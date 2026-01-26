package com.kh.osori.trans.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.osori.trans.dao.MytransDao;
import com.kh.osori.trans.model.vo.Mytrans;

@Service
public class MytransServiceImpl implements MytransService{
	
	@Autowired
	private MytransDao dao;
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	public int myTransSave(Mytrans mt) {
		
		return dao.myTransSave(sqlSession,mt);
	}

	public List<Mytrans> getMyTransactions(int userId) {
		return dao.selectMyTrans(sqlSession, userId);
	}



}
