package com.kh.osori.challenges.service;

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
	int joinGroupChallenge(GroupChall groupChall); 
	
	List<GroupChall> getGroupJoinList(int groupbId);

	int failActiveZeroChallenge(int groupBId);
	
	List<Map<String, Object>> getGroupRanking(int groupbId, String challengeId);


	List<GroupChall> getGroupPastChallengeList(int groupbId);


	void closeExpiredChallenges();


	
	
	
	
}
