package com.kh.osori.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kh.osori.challenges.service.ChallengeServiceImpl;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChallengeStatusSchedulerConfig {

  private final ChallengeServiceImpl challengeService;

  // 테스트용(매분 0초)
  @Scheduled(cron = "0 */1 * * * *")
  public void updateMyChallStatus() {

    // 1) RESERVED -> PROCEEDING (시작일 도래)
    challengeService.promoteReservedToProceeding();
    
    // 1.5) 진행 중인데 이미 조건 위반이면 즉시 FAILED 처리
    challengeService.closeViolatedProceedingToFailed();

    // 2) 종료된 PROCEEDING -> SUCCESS/FAILED 판정 후 업데이트
    challengeService.closeExpiredProceedingToResult();
  }
  
  
  //(그룹) 챌린지 종료일이 지난 데이터를 SUCCESS로 변경
  //실제엔 0 0으로(every hour) 변경할것
  @Scheduled(cron = "0 * * * * *")
  public void checkChallengeExpiry() {
      System.out.println("[SCHED] checkChallengeExpiry tick");
      challengeService.runGroupChallengeScheduler();
  }

}

