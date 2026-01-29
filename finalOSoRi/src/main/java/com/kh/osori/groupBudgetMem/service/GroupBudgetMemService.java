package com.kh.osori.groupBudgetMem.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.osori.groupBudgetMem.dao.GroupMemBudgetDao;
import com.kh.osori.groupBudgetMem.model.vo.GroupBudgetMem;

@Service
public class GroupBudgetMemService {
	
	@Autowired
	private GroupMemBudgetDao dao;
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	public List<GroupBudgetMem> searchGroupMem(int groupId) {
		
		return dao.searchGroupMem(sqlSession,groupId);
	}
	
	

}
