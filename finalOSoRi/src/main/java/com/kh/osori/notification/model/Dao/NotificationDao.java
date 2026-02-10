package com.kh.osori.notification.model.Dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.osori.notification.model.vo.Notification;

@Repository
public class NotificationDao {

	public int insertNotification(SqlSessionTemplate sqlSession, Notification noti) {
		return sqlSession.insert("userMapper.insertNotification", noti);
	}

	public List<String> getGroupMemberIds(SqlSessionTemplate sqlSession, int groupbId) {
		return sqlSession.selectList("groupBudgetMapper.getGroupMemberIds",groupbId);
	}

}
