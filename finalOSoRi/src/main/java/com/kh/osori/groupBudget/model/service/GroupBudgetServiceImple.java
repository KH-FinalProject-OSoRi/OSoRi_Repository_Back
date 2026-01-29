package com.kh.osori.groupBudget.model.service;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.osori.challenges.model.vo.Challenges;
import com.kh.osori.challenges.model.vo.GroupChall;
import com.kh.osori.groupBudget.model.dao.GroupBudgetDao;
import com.kh.osori.groupBudget.model.vo.BudgetMem;
import com.kh.osori.groupBudget.model.vo.GroupBudget;
import com.kh.osori.notification.model.vo.Notification;
import com.kh.osori.trans.model.vo.Grouptrans;
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
	public List<GroupBudget> oldGroupBudgetList(int userId) {
		return dao.oldGroupBudgetList(sqlSession,userId);
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
	public int deleteGroupBudget(int groupbId) {
		return dao.deleteGroupBudget(sqlSession,groupbId);
	}

	@Override
	public int updateGroupB(GroupBudget gb) {
		return dao.updateGroupB(sqlSession,gb);
	}
	
	@Override
	public void deleteUser() {
		
	}

	@Override
	public String getAdminId(int groupbId) {
		return dao.getAdminId(sqlSession,groupbId);
	}

	@Override
	public String getLoginid(int userId) {
		return dao.getLoginid(sqlSession,userId);
	}

	@Override
	public int updateInviStatus(BudgetMem update) {
		return dao.updateInviStatus(sqlSession,update);
	}

	@Override
	public int updateIsRead(int notiId) {
		return dao.updateIsRead(sqlSession,notiId);
	}

	@Override
	public List<Notification> notiList(String loginId) {
		return dao.notiList(sqlSession,loginId);
	}

	@Override
	public List<Grouptrans> groupTransactionList(int groupbId) {
		return dao.groupTransactionList(sqlSession, groupbId);
	}

	@Override
	public int groupCheckAdmin(int groupbId) {
		return dao.groupCheckAdmin(sqlSession,groupbId);
	}

	@Override
	public List<Challenges> groupChallList() {
		return dao.groupChallList(sqlSession);
	}

	@Override
	public int addGroupChall(GroupChall chall) {
		return dao.addGroupChall(sqlSession,chall);
	}

}
