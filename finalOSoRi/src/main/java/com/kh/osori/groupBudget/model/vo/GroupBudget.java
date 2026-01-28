package com.kh.osori.groupBudget.model.vo;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GroupBudget {
	private int groupbId;
	private int userId;
	private String title;
	@JsonProperty("bAmount")
	private int bAmount;
	private Date startDate;
	private Date endDate;
	private Date createdAt;
	private String status;
}
