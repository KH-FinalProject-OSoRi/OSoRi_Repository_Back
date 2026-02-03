package com.kh.osori.badge.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BadgeDao {

    @Autowired
    private SqlSessionTemplate sqlSession;

    // 뱃지 지급
    public int insertDefaultBadge(int userNo, int badgeNo) {
        Map<String, Object> params = new HashMap();
        params.put("userNo", userNo);
        params.put("badgeNo", badgeNo);
        return sqlSession.insert("badgeMapper.insertDefaultBadge", params);
    }

    // 뱃지 조회
    public List<Map<String, Object>> selectUserBadges(int userNo) {
        return sqlSession.selectList("badgeMapper.selectUserBadges", userNo);
    }
}