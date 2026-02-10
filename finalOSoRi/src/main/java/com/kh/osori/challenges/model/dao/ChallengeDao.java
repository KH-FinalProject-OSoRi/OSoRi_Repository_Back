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

	
	//------------------------------------------------
	//	그룹챌린지
	
	//그룹챌린지 차원의 테이블 status ="proceeding"추가
	public int joinGroupChallenge(SqlSessionTemplate sqlSession, GroupChall groupChall) {
		return sqlSession.insert("challengeMapper.joinGroupChallenge", groupChall);
	}
	
	// 참여 시 우저별 result 테이블에도 status = "proceeding"
	public int joinGroupChallResult(SqlSessionTemplate sqlSession, Map<String, Object> param) {
		return sqlSession.insert("challengeMapper.joinGroupChallResult", param);
	}

	// 무지출 챌린지 중 지출 발생 "즉시" 전체 result 테이블 status failed 처리
	public int failUserOnZeroChallengeExpense(SqlSessionTemplate sqlSession, Map<String, Object> param) {
		return sqlSession.update("challengeMapper.failUserOnZeroChallengeExpense", param);
	}

	// 무지출 챌린지 종료 까지 proceeding인 경우 (failed가 안 된 경우)  result 테이블 success 처리
	public int successRemainingZeroChallengeUsers(SqlSessionTemplate sqlSession) {
		return sqlSession.update("challengeMapper.successRemainingZeroChallengeUsers");
	}

	// 스케쥴러용. 기간 지났는데 proceeding인 경우 정산
	public List<Map<String, Object>> selectEndedCompetitionChallenges(SqlSessionTemplate sqlSession) {
		return sqlSession.selectList("challengeMapper.selectEndedCompetitionChallenges");
	}
	
	//실시간 지출 순위 계산
		public List<Map<String, Object>> getGroupRanking(SqlSessionTemplate sqlSession, Map<String, Object> params) {
			 return sqlSession.selectList("challengeMapper.getGroupRanking", params);
		}

	// 유저별 TOTAL_AMOUNT 계산 업데이트
	public int updateCompetitionTotals(SqlSessionTemplate sqlSession, Map<String, Object> param) {
		return sqlSession.update("challengeMapper.updateCompetitionTotals", param);
	}

	// 순위 계산 업데이트
	public int updateCompetitionRanks(SqlSessionTemplate sqlSession, Map<String, Object> param) {
		return sqlSession.update("challengeMapper.updateCompetitionRanks", param);
	}

	// 1등만 SUCCESS, 나머지 FAILED 확정
	public int finalizeCompetitionStatus(SqlSessionTemplate sqlSession, Map<String, Object> param) {
		return sqlSession.update("challengeMapper.finalizeCompetitionStatus", param);
	}

	// success인 유저 중 뱃지 발급 대상인지 확인(중복 방지)
	public List<Map<String, Object>> selectUsersToRewardFromResult(SqlSessionTemplate sqlSession) {
		return sqlSession.selectList("challengeMapper.selectUsersToRewardFromResult");
	}

	// 발급 대상한테 뱃지 발급
	public int mergeUserBadge(SqlSessionTemplate sqlSession, Map<String, Object> param) {
		return sqlSession.insert("challengeMapper.mergeUserBadge", param);
	}

    
    
	
	

	public List<GroupChall> getGroupJoinedList(SqlSessionTemplate sqlSession, int groupbId, int userId) {
	    Map<String, Object> params = new HashMap<>();
	    params.put("groupbId", groupbId);
	    params.put("userId", userId);
	    
	    // params 맵을 하나의 인자로 전달
	    return sqlSession.selectList("challengeMapper.getGroupJoinedList", params);
	}

	

	public List<Map<String, Object>> getGroupPastChallengeList(SqlSessionTemplate sqlSession, Map<String, Object> params) {
	    return sqlSession.selectList("challengeMapper.getGroupPastChallengeList", params);
	}
	
	public int closeExpiredGroupChallenges(SqlSessionTemplate sqlSession) {
	    return sqlSession.update("challengeMapper.closeExpiredGroupChallenges");
	}

}


