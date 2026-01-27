package com.kh.osori.groupBudget.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BudgetMem {
	private int groupbId;
	private int userId;
	private String role;
	private String status;
}
