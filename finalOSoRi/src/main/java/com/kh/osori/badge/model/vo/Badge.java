package com.kh.osori.badge.model.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class Badge {
	
	    private int badgeId;
	    private String badgeName;
	    private String badgeIconUrl;
	    private String challengeId;
	    private Date earnedAt;

	    private String challengeDesc; 
	    private String groupBudgetTitle;

	
}
