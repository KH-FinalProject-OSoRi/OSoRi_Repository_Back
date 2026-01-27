package com.kh.osori.trans.service;

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



}
