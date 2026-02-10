package com.kh.osori.trans.model.vo;

import java.sql.Date;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Grouptrans {
	
	private String type; //수입/지출
	private int transId; //지출번호
	private String title; //가게명 or 거래내역
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date transDate; //거래날짜
	private int originalAmount; //금액
	private String category; //카테고리
	private String memo; //메모
	private int groupBId; //소속된 그룹가계부 id
	private int userId; //수입지출 입력한사랑
	private String nickname;
	public String getTransType() {
		return this.type;
	}
}
