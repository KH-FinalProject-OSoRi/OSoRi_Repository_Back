package com.kh.osori.challenges.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kh.osori.challenges.model.vo.Challenge;
import com.kh.osori.challenges.model.vo.GroupChall;
import com.kh.osori.challenges.model.vo.MyChall;
import com.kh.osori.challenges.model.vo.MyChallHistory;

public interface ChallengeService {
	
	ArrayList<Challenge> getChallengeList(String challengeMode);
	

	int joinMyChallenge(MyChall myChall); 
	
	ArrayList<MyChall> getMyChallengeList(HashMap<String, Object> req);

	// 지난 챌린지 목록 조회 (END_DATE 지난 것만)
	ArrayList<MyChallHistory> getMyPastChallengeList(HashMap<String, Object> req);
		
	// 참여하기 전 잔액(수입-지출) 검증 포함
	int joinMyChallengeWithBalanceCheck(MyChall myChall, Integer expenseSum, Integer expenseCount);
		
	// 스케줄러용: 상태 자동 갱신
	int promoteReservedToProceeding();
	//int closeExpiredProceedingToFailed();

//	그룹챌린지용
	
	// ✅ 그룹 챌린지 참여(기존) - 내부에서 결과 테이블도 초기화하도록 확장
    int joinGroupChallenge(GroupChall groupChall);

    // ✅ 스케줄러: 그룹 챌린지 종료/결과 확정/뱃지 지급까지 한 번에
    void runGroupChallengeScheduler();


	int handleZeroChallengeExpense(int groupbId, int userId, Date transDate);
	
//	int joinGroupChallenge(GroupChall groupChall); 
//	
	List<GroupChall> getGroupJoinedList(int groupbId, int userId);
//
//	int failActiveZeroChallenge(int groupBId);
//	
	List<Map<String, Object>> getGroupRanking(int groupbId, String challengeId);
//
	List<Map<String, Object>> getGroupPastChallengeList(int groupbId, int userId);
//
//	void closeExpiredChallenges();

	
	


	
	
	
	
}
