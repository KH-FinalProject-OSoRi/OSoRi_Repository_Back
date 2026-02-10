package com.kh.osori.badge.model.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class UserBadge {

	    private int userId;
	    private int badgeId;
	    private Date earnedAt; // 획득 날짜
	

}
