package com.kh.osori.challenges.model.vo;
import java.sql.Date;
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
	private Date startDate;
	private Date endDate;
}
