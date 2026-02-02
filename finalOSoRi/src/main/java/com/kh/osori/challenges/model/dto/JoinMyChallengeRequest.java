package com.kh.osori.challenges.model.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class JoinMyChallengeRequest {

  private String challengeId;
  private int userId;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate startDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate endDate;

  // 프론트 파일(mockData 등) 기준 지출 합계/건수
  // - 지출이 1건 이상 있어야 잔액 계산이 의미있다고 판단
  private Integer expenseSum;   // 지출 합계 (원)
  private Integer expenseCount; // 지출 건수
}
