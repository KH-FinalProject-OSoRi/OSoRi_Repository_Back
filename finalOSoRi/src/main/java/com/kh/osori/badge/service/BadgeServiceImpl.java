package com.kh.osori.badge.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.osori.badge.dao.BadgeDao;
import com.kh.osori.badge.model.vo.Badge;

@Service
public class BadgeServiceImpl implements BadgeService {

    @Autowired
    private BadgeDao badgeDao;

    @Override
    public List<Badge> selectUserBadges(int userId) {
        return badgeDao.selectUserBadges(userId);
    }

    @Override
    public int insertDefaultBadge(int userId, int badgeId) {
        return badgeDao.insertDefaultBadge(userId, badgeId);
    }
}