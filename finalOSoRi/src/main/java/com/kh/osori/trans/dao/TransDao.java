package com.kh.osori.trans.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.osori.trans.model.vo.Grouptrans;
import com.kh.osori.trans.model.vo.Mytrans;

@Repository
public class TransDao {

	public int myTransSave(SqlSessionTemplate sqlSession, Mytrans mt) {
	
		return sqlSession.insert("transMapper.myTransSave",mt);
	}

	public int groupTransSave(SqlSessionTemplate sqlSession, Grouptrans gt) {

		return sqlSession.insert("transMapper.groupTransSave",gt);
	}

}
