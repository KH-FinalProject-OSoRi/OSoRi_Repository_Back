package com.kh.osori.challenges.model.vo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Challenges {
	private String challengeId;
	private String description;
	private int target;
	private int duration;
	private String category;
	private String type;
	private String status;
	private int targetCount;
	private String challengeMode;
}
