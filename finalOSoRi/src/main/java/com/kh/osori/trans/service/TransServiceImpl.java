package com.kh.osori.trans.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.osori.trans.dao.TransDao;
import com.kh.osori.trans.model.vo.Grouptrans;
import com.kh.osori.trans.model.vo.Mytrans;

@Service
public class TransServiceImpl implements TransService{
	
	@Autowired
	private TransDao dao;
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	public int myTransSave(Mytrans mt) {
		
		return dao.myTransSave(sqlSession,mt);
	}

	public int GroupTransSave(Grouptrans gt) {
		
		return dao.groupTransSave(sqlSession,gt);
	}
	
	public List<Mytrans> getMyTransactions(int userId) {
		return dao.selectMyTrans(sqlSession, userId);
	}

	public int updateTrans(Mytrans mt) {
		
		return dao.updateTrans(sqlSession,mt);
	}

	public int deleteTrans(int transId) {
		
		return dao.deleteTrans(sqlSession,transId);
	}



}
