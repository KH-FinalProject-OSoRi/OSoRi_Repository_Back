package com.kh.osori.trans.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.osori.trans.model.vo.Mytrans;

@Repository
public class MytransDao {

	public int myTransSave(SqlSessionTemplate sqlSession, Mytrans mt) {
	
		return sqlSession.insert("transMapper.myTransSave",mt);
	}

}
