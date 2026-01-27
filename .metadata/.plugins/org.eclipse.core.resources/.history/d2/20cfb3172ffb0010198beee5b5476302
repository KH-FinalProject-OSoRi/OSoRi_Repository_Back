package com.kh.osori.groupBudget.model.service;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.osori.groupBudget.model.dao.GroupBudgetDao;
import com.kh.osori.groupBudget.model.vo.BudgetMem;
import com.kh.osori.groupBudget.model.vo.GroupBudget;
import com.kh.osori.groupBudget.model.vo.GroupTrans;
import com.kh.osori.user.model.vo.User;

@Service
public class GroupBudgetServiceImple implements GroupBudgetService {
	
	@Autowired
	private GroupBudgetDao dao;
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public List<GroupBudget> groupBudgetList(int userId) {
		return dao.groupBudgetList(sqlSession,userId);
		
	}
	
	@Override
	public int addNewGroupB(GroupBudget gb) {
		return dao.addNewGroupB(sqlSession,gb);
		
	}
	
	@Override
	public int addGroupAdmin(BudgetMem newMem) {
		return dao.addGroupAdmin(sqlSession,newMem);
	}
	
	@Override
	public int addGroupMember(BudgetMem mem) {
		return dao.addGroupMember(sqlSession,mem);
	}
	
	@Override
	public List<User> searchGroupMemberList(String keyword) {
		return dao.searchGroupMemberList(sqlSession,keyword);
	}

	@Override
	public void editBAmount() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUser() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteGroupBudget() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateGroupBudget() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<GroupTrans> groupTransactionList(int groupbId) {
		return dao.groupTransactionList(sqlSession, groupbId);
	}

}
