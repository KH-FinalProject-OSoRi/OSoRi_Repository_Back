package com.kh.osori.groupBudgetMem.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupBudgetMem {
	private int groupBId;
	private int userId;
	private String role;
	private String status;
	private String nickName;

}
