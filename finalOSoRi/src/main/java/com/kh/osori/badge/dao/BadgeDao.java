package com.kh.osori.badge.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.osori.badge.model.vo.Badge;

@Repository
public class BadgeDao {

    @Autowired
    private SqlSessionTemplate sqlSession;

    // 뱃지 지급
    public int insertDefaultBadge(int userId, int badgeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("badgeId", badgeId);
        return sqlSession.insert("badgeMapper.insertDefaultBadge", params);
    }

    // 뱃지 조회
    public List<Badge> selectUserBadges(int userId) {
        return sqlSession.selectList("badgeMapper.mergeUserBadge", userId);
    }
}