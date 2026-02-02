package com.kh.osori.challenges.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.osori.challenges.model.vo.Challenge;
import com.kh.osori.challenges.model.vo.GroupChall;
import com.kh.osori.challenges.model.vo.MyChall;
import com.kh.osori.challenges.model.vo.MyChallHistory;

@Repository
public class ChallengeDao {
	
	public ArrayList<Challenge> getChallengeList(SqlSessionTemplate sqlSession, String challengeMode) {
		return (ArrayList) sqlSession.selectList("challengeMapper.getChallengeList", challengeMode);
	}

	public int joinMyChallenge(SqlSessionTemplate sqlSession, MyChall myChall) {
		return sqlSession.insert("challengeMapper.joinMyChallenge", myChall);
	}

	public ArrayList<MyChall> getMyChallengeList(SqlSessionTemplate sqlSession, HashMap<String, Object> req) {
		return (ArrayList) sqlSession.selectList("challengeMapper.getMyChallengeList", req);
	}

	public ArrayList<MyChallHistory> getMyPastChallengeList(SqlSessionTemplate sqlSession, HashMap<String, Object> req) {
		return (ArrayList) sqlSession.selectList("challengeMapper.getMyPastChallengeList", req);
	}

	// ✅ 단건 챌린지 조회
	public Challenge selectChallengeById(SqlSessionTemplate sqlSession, String challengeId) {
		return sqlSession.selectOne("challengeMapper.selectChallengeById", challengeId);
	}

	// ✅ MYTRANS 수입 요약 (기간)
	public HashMap<String, Object> getIncomeSummaryByRange(SqlSessionTemplate sqlSession, HashMap<String, Object> req) {
		return sqlSession.selectOne("challengeMapper.getIncomeSummaryByRange", req);
	}

	// ✅ MYTRANS 지출 요약 (기간)
	public HashMap<String, Object> getExpenseSummaryByRange(SqlSessionTemplate sqlSession, HashMap<String, Object> req) {
		return sqlSession.selectOne("challengeMapper.getExpenseSummaryByRange", req);
	}

	// =========================
	// 스케줄러 / 성공판정용 쿼리
	// =========================

	// ✅ 스케줄러: 예약 -> 진행중
	public int promoteReservedToProceeding(SqlSessionTemplate sqlSession) {
		return sqlSession.update("challengeMapper.promoteReservedToProceeding");
	}

	// ✅ 스케줄러: 종료된 진행중 챌린지 목록 (SUCCESS/FAILED 판정 대상)
	public ArrayList<MyChallHistory> selectEndedProceedingChallenges(SqlSessionTemplate sqlSession) {
		return (ArrayList) sqlSession.selectList("challengeMapper.selectEndedProceedingChallenges");
	}

	// ✅ 스케줄러: MYCHALL 상태 업데이트 (동일 challengeId라도 기간별로 구분)
	public int updateMyChallStatus(SqlSessionTemplate sqlSession, HashMap<String, Object> param) {
		return sqlSession.update("challengeMapper.updateMyChallStatus", param);
	}

	// ✅ 기간 내 지출 합계 (카테고리 선택)
	public HashMap<String, Object> getExpenseSumByRange(SqlSessionTemplate sqlSession, HashMap<String, Object> req) {
		return sqlSession.selectOne("challengeMapper.getExpenseSumByRange", req);
	}

	// ✅ 기간 내 지출 건수 (카테고리 선택)
	public HashMap<String, Object> getExpenseCountByRange(SqlSessionTemplate sqlSession, HashMap<String, Object> req) {
		return sqlSession.selectOne("challengeMapper.getExpenseCountByRange", req);
	}

	// ✅ 기간 내 "일자별" 지출 합계 (하루당 제한형)
	public ArrayList<HashMap<String, Object>> getDailyExpenseSumsByRange(SqlSessionTemplate sqlSession, HashMap<String, Object> req) {
		return (ArrayList) sqlSession.selectList("challengeMapper.getDailyExpenseSumsByRange", req);
	}
	
	// ✅ 스케줄러: 진행 중이면서 아직 종료되지 않은 챌린지 목록 (즉시 실패 판정 대상)
	public ArrayList<MyChallHistory> selectActiveProceedingChallenges(SqlSessionTemplate sqlSession) {
	    return (ArrayList) sqlSession.selectList("challengeMapper.selectActiveProceedingChallenges");
	}

//	그룹챌린지
	
	public int joinGroupChallenge(SqlSessionTemplate sqlSession, GroupChall groupChall) {
		return sqlSession.insert("challengeMapper.joinGroupChallenge", groupChall);
	}

	public List<GroupChall> getGroupJoinList(SqlSessionTemplate sqlSession, int groupbId) {
		return sqlSession.selectList("challengeMapper.getGroupJoinList", groupbId);
	}

	public int failActiveZeroChallenge(SqlSessionTemplate sqlSession, int groupbId) {
		return sqlSession.update("challengeMapper.failActiveZeroChallenge", groupbId);
		
	}
	
	public List<Map<String, Object>> selectCompetitionRanking(SqlSessionTemplate sqlSession, Map<String, Object> params) {
	    return sqlSession.selectList("challengeMapper.selectCompetitionRanking", params);
	}

	public List<GroupChall> getGroupPastChallengeList(SqlSessionTemplate sqlSession, int groupbId) {
		return sqlSession.selectList("challengeMapper.getGroupPastChallengeList", groupbId);
	}
	
	public List<Map<String, Object>> getGroupRanking(SqlSessionTemplate sqlSession, Map<String, Object> params) {
	    return sqlSession.selectList("challengeMapper.selectCompetitionRanking", params);
	}

}


//package com.kh.osori.challenges.model.dao;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.mybatis.spring.SqlSessionTemplate;
//import org.springframework.stereotype.Repository;
//
//import com.kh.osori.challenges.model.vo.Challenge;
//import com.kh.osori.challenges.model.vo.MyChall;
//import com.kh.osori.challenges.model.vo.MyChallHistory;
//
//@Repository
//public class ChallengeDao {
//	
//	public ArrayList<Challenge> getChallengeList(SqlSessionTemplate sqlSession, String challengeMode) {
//		return (ArrayList) sqlSession.selectList("challengeMapper.getChallengeList", challengeMode);
//	}
//
//	public int joinMyChallenge(SqlSessionTemplate sqlSession, MyChall myChall) {
//		return sqlSession.insert("challengeMapper.joinMyChallenge", myChall);
//	}
//
//	public ArrayList<MyChall> getMyChallengeList(SqlSessionTemplate sqlSession, HashMap<String, Object> req) {
//		return (ArrayList) sqlSession.selectList("challengeMapper.getMyChallengeList", req);
//	}
//
//	public ArrayList<MyChallHistory> getMyPastChallengeList(SqlSessionTemplate sqlSession, HashMap<String, Object> req) {
//		return (ArrayList) sqlSession.selectList("challengeMapper.getMyPastChallengeList", req);
//	}
//
//	// ✅ 단건 챌린지 조회
//	public Challenge selectChallengeById(SqlSessionTemplate sqlSession, String challengeId) {
//		return sqlSession.selectOne("challengeMapper.selectChallengeById", challengeId);
//	}
//
//	// ✅ MYTRANS 수입 요약 (기간)
//	public HashMap<String, Object> getIncomeSummaryByRange(SqlSessionTemplate sqlSession, HashMap<String, Object> req) {
//		return sqlSession.selectOne("challengeMapper.getIncomeSummaryByRange", req);
//	}
//
//	// ✅ MYTRANS 지출 요약 (기간)
//	public HashMap<String, Object> getExpenseSummaryByRange(SqlSessionTemplate sqlSession, HashMap<String, Object> req) {
//		return sqlSession.selectOne("challengeMapper.getExpenseSummaryByRange", req);
//	}
//	
//	
////	public ArrayList<Challenge> getChallengeList(SqlSessionTemplate sqlSession, String challengeMode) {
////		return (ArrayList) sqlSession.selectList("challengeMapper.getChallengeList", challengeMode);
////	}
////
////	public int joinMyChallenge(SqlSessionTemplate sqlSession, MyChall myChall) {
////		return sqlSession.insert("challengeMapper.joinMyChallenge", myChall); 
////	}
////
////	public ArrayList<MyChall> getMyChallengeList(SqlSessionTemplate sqlSession, HashMap<String, Object> req) {
////		return (ArrayList) sqlSession.selectList("challengeMapper.getMyChallengeList", req);
////	}
////	
////	// ✅ 챌린지 1건 조회 (필요금액 계산용)
////	public Challenge getChallengeById(SqlSessionTemplate sqlSession, String challengeId) {
////		return sqlSession.selectOne("challengeMapper.getChallengeById", challengeId);
////	}
////		
////	// ✅ 수입 요약 (DB 기준)
////	public Map<String, Object> getIncomeSummary(SqlSessionTemplate sqlSession, int userId) {
////		return sqlSession.selectOne("challengeMapper.getIncomeSummary", userId);
////	}
////
////	// [ADDED] 기간(from~to) 기준 수입 요약 (DB 기준)
////	public Map<String, Object> getIncomeSummaryByRange(SqlSessionTemplate sqlSession, Map<String, Object> param) {
////		return sqlSession.selectOne("challengeMapper.getIncomeSummaryByRange", param);
////	}
////
////	// [ADDED] 기간(from~to) 기준 지출 요약 (DB 기준)
////	public Map<String, Object> getExpenseSummaryByRange(SqlSessionTemplate sqlSession, Map<String, Object> param) {
////		return sqlSession.selectOne("challengeMapper.getExpenseSummaryByRange", param);
////	}
////		
////	// ✅ 지난 챌린지 목록
////	public ArrayList<MyChallHistory> getMyPastChallengeList(SqlSessionTemplate sqlSession, HashMap<String, Object> req) {
////		return (ArrayList) sqlSession.selectList("challengeMapper.getMyPastChallengeList", req);
////	}
////
////	// ✅ 스케줄러: 예약 -> 진행중
////	public int promoteReservedToProceeding(SqlSessionTemplate sqlSession) {
////		return sqlSession.update("challengeMapper.promoteReservedToProceeding");
////	}
////
////	// ✅ 스케줄러: 진행중 -> 실패 (기간 종료)
////	public int closeExpiredProceedingToFailed(SqlSessionTemplate sqlSession) {
////		return sqlSession.update("challengeMapper.closeExpiredProceedingToFailed");
////	}
//
//	/*
//	public ArrayList<Challenge> getChallengeList(SqlSessionTemplate sqlSession, String challengeMode) {
//		return (ArrayList) sqlSession.selectList("challengeMapper.getChallengeList", challengeMode);
//	}
//
//	public int joinMyChallenge(SqlSessionTemplate sqlSession, MyChall myChall) {
//		return sqlSession.insert("challengeMapper.joinMyChallenge", myChall); 
//	}
//
//	public ArrayList<MyChall> getMyChallengeList(SqlSessionTemplate sqlSession, HashMap<String, Object> req) {
//		return (ArrayList) sqlSession.selectList("challengeMapper.getMyChallengeList", req);
//	}
//	
//	// ✅ 챌린지 1건 조회 (필요금액 계산용)
//	public Challenge getChallengeById(SqlSessionTemplate sqlSession, String challengeId) {
//		return sqlSession.selectOne("challengeMapper.getChallengeById", challengeId);
//	}
//		
//	// ✅ 수입 요약 (DB 기준)
//	public Map<String, Object> getIncomeSummary(SqlSessionTemplate sqlSession, int userId) {
//		return sqlSession.selectOne("challengeMapper.getIncomeSummary", userId);
//	}
//		
//	// ✅ 지난 챌린지 목록
//	public ArrayList<MyChallHistory> getMyPastChallengeList(SqlSessionTemplate sqlSession, HashMap<String, Object> req) {
//		return (ArrayList) sqlSession.selectList("challengeMapper.getMyPastChallengeList", req);
//	}
//
//	// ✅ 스케줄러: 예약 -> 진행중
//	public int promoteReservedToProceeding(SqlSessionTemplate sqlSession) {
//		return sqlSession.update("challengeMapper.promoteReservedToProceeding");
//	}
//
//	// ✅ 스케줄러: 진행중 -> 실패 (기간 종료)
//	public int closeExpiredProceedingToFailed(SqlSessionTemplate sqlSession) {
//		return sqlSession.update("challengeMapper.closeExpiredProceedingToFailed");
//	}
//	*/
//
//}
