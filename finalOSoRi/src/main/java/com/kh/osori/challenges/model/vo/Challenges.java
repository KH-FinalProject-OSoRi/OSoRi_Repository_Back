package com.kh.osori.challenges.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Challenges {
	
	private String challengeId; // 챌린지 명 
	private String description; // 챌린지 상세 설명
	private int target; // 챌린지 목표 금액
	private int duration; // 챌린지 기간
	private String category; // 카테고리
	private String type; // 수입/수출 타입
	private String status; // 상태 
	private int targetCount; // 목표 횟수
	private String challengeMode; // 챌린지 모드(개인,그룹)

}
