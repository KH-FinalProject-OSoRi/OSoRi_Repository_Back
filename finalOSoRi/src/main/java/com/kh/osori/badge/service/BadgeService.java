package com.kh.osori.badge.service;

import java.util.List;

import com.kh.osori.badge.model.vo.Badge;

public interface BadgeService {
	
    List<Badge> selectUserBadges(int userId);
    
    int insertDefaultBadge(int userId, int badgeId);
    
}