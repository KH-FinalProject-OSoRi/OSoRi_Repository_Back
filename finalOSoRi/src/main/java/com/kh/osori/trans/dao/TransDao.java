package com.kh.osori.trans.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.osori.trans.model.vo.Grouptrans;
import com.kh.osori.trans.model.vo.Mytrans;

@Repository
public class TransDao {

	public int myTransSave(SqlSessionTemplate sqlSession, Mytrans mt) {
		System.out.println("TransDao mt : "+mt);
		return sqlSession.insert("transMapper.myTransSave",mt);
	}

	public int groupTransSave(SqlSessionTemplate sqlSession, Grouptrans gt) {

		return sqlSession.insert("transMapper.groupTransSave",gt);
	}
	
	public List<Mytrans> selectMyTrans(SqlSessionTemplate sqlSession, int userId) {
		return sqlSession.selectList("transMapper.selectMyTrans", userId);
	}

	public int updateTrans(SqlSessionTemplate sqlSession, Mytrans mt) {
	
		return sqlSession.update("transMapper.updateTrans",mt);
	}

	public int deleteTrans(SqlSessionTemplate sqlSession, int transId) {
		
		return sqlSession.delete("transMapper.deleteTrans",transId);
	}
	
	// [추가] 고정지출 -> MYTRANS 자동반영 MERGE 실행
	public int mergeFixedToMyTrans(SqlSessionTemplate sqlSession) {
	  return sqlSession.insert("transMapper.mergeFixedToMyTrans");
	}

	public List<Grouptrans> getGroupTransactions(SqlSessionTemplate sqlSession, int groupId) {
		
		return sqlSession.selectList("transMapper.selectGroupTrans",groupId);
	}

	public int updateGroupTrans(SqlSessionTemplate sqlSession, Grouptrans gt) {
		
		return sqlSession.update("transMapper.updateGroupTrans",gt);
	}

	public int deleteGroupTrans(SqlSessionTemplate sqlSession, int transId) {
		
		return sqlSession.delete("transMapper.deleteGroupTrans",transId);
	}

	public Object groupInfo(SqlSessionTemplate sqlSession, int groupId) {
		
		return sqlSession.selectOne("transMapper.groupInfo",groupId);
	}

	public List<Mytrans> recentTrans(SqlSessionTemplate sqlSession, int userId) {
		
		return sqlSession.selectList("transMapper.recentTrans",userId);
	}

	

}
