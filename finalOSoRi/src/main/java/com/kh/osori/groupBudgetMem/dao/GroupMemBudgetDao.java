package com.kh.osori.groupBudgetMem.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.osori.groupBudgetMem.model.vo.GroupBudgetMem;

@Repository
public class GroupMemBudgetDao {

	public List<GroupBudgetMem> searchGroupMem(SqlSessionTemplate sqlSession, int groupId) {
		
		return sqlSession.selectList("groupBudgetMem.searchGroupMem",groupId);
	}

}
