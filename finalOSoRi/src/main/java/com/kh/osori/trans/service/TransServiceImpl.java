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

	@Override
	public int myTransSave(Mytrans mt) {
		
		return dao.myTransSave(sqlSession,mt);
	}

	@Override
	public int GroupTransSave(Grouptrans gt) {
		
		return dao.groupTransSave(sqlSession,gt);
	}
	
	@Override
	public List<Mytrans> getMyTransactions(int userId) {
		return dao.selectMyTrans(sqlSession, userId);
	}

	@Override
	public int updateTrans(Mytrans mt) {
		
		return dao.updateTrans(sqlSession,mt);
	}

	@Override
	public int deleteTrans(int transId) {
		
		return dao.deleteTrans(sqlSession,transId);
	}

	@Override
	public List<Grouptrans> getGroupTransactions(int groupId) {
		return dao.getGroupTransactions(sqlSession,groupId);
	}
	
	@Override
	public int updateGroupTrans(Grouptrans gt) {
			
			return dao.updateGroupTrans(sqlSession,gt);
		}

	@Override
	public int deleteGroupTrans(int transId) {
		
		return dao.deleteGroupTrans(sqlSession,transId);
	}
	
	@Override
	// [추가] 고정지출 -> MYTRANS 자동반영 MERGE 실행
	public int mergeFixedToMyTrans() {
		return dao.mergeFixedToMyTrans(sqlSession);
	}

	@Override
	public Object groupInfo(int groupId) {
		
		return dao.groupInfo(sqlSession,groupId);
	}

	@Override
	public List<String> getGroupMemberIds(int groupBId) {
		return dao.getGroupMemberIds(sqlSession,groupBId);
	}

	@Override
	public String getLoginId(int userId) {
		return dao.getLoginId(sqlSession, userId);
	}

	public String getGroupTitle(int groupBId) {
		return dao.getGroupTitle(sqlSession, groupBId);
	}

	public List<Mytrans> recentTrans(int userId) {
		
		return dao.recentTrans(sqlSession,userId);
	}


}
