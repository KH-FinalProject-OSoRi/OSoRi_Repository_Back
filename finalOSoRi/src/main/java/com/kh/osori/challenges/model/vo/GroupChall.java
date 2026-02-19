package com.kh.osori.challenges.model.vo;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GroupChall {
	private String challengeId;
	private int groupbId;
	private String status;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	
	private String description; 
    private String category;
    private String type;
    private int duration;
    private int target;
    private int targetCount;
}
