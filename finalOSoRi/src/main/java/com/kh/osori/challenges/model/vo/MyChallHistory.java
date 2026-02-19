package com.kh.osori.challenges.model.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MyChallHistory {

  private String challengeId;
  private int userId;

  // SUCCESS / FAILED (조회 시점에 END_DATE가 지났는데 PROCEEDING/RESERVED이면 FAILED로 내려주도록 SQL에서 보정함)
  private String status;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime startDate; 

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime endDate;

  // CHALLENGES 정보 (지난 챌린지 화면에서 보여주기 위함)
  private String description;
  private String category;
  private String type;
  private int duration;
  private int target;
  private int targetCount;
  private String challengeMode;
}
