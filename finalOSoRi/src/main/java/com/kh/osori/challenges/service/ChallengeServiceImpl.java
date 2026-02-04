package com.kh.osori.challenges.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.osori.badge.dao.BadgeDao;
import com.kh.osori.badge.service.BadgeService;
import com.kh.osori.challenges.model.dao.ChallengeDao;

import com.kh.osori.challenges.model.vo.Challenge;
import com.kh.osori.challenges.model.vo.GroupChall;
import com.kh.osori.challenges.model.vo.MyChall;
import com.kh.osori.challenges.model.vo.MyChallHistory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

	@Autowired
	private final ChallengeDao dao;
	
	@Autowired
	private final BadgeDao badgeDao;
	
	@Autowired
	private final BadgeService badgeService;
	
	@Autowired
	private final SqlSessionTemplate sqlSession;

	@Override
	public ArrayList<Challenge> getChallengeList(String challengeMode) {
		return dao.getChallengeList(sqlSession, challengeMode);
	}

	@Override
	public int joinMyChallenge(MyChall myChall) {
		return dao.joinMyChallenge(sqlSession, myChall);
	}

	@Override
	public ArrayList<MyChall> getMyChallengeList(HashMap<String, Object> req) {
		return dao.getMyChallengeList(sqlSession, req);
	}

	@Override
	public ArrayList<MyChallHistory> getMyPastChallengeList(HashMap<String, Object> req) {
		return dao.getMyPastChallengeList(sqlSession, req);
	}

	
	// MYTRANS(내역) 기준으로 서버에서 직접 계산함
	public int joinMyChallengeWithBalanceCheck(
			MyChall myChall,
			Integer expenseSumFromClient,
			Integer expenseCountFromClient
	) {

		// 0) 챌린지 존재 확인
		Challenge challenge = dao.selectChallengeById(sqlSession, myChall.getChallengeId());
		if (challenge == null) {
			throw new IllegalArgumentException("존재하지 않는 챌린지입니다.");
		}

		// 1) 검증기간 계산
		// - "챌린지 시작일이 속한 달" 기준으로 시작일 전날까지 수입/지출/잔액을 계산함
		LocalDate start = toLocalDate(myChall.getStartDate());
		Map<String, String> range = calcCheckRange(start); // fromDate/toDate 문자열 (YYYY-MM-DD)

		// 2) MYTRANS 기준 수입/지출 요약 조회
		HashMap<String, Object> incomeReq = new HashMap<>();
		incomeReq.put("userId", myChall.getUserId());
		incomeReq.put("fromDate", range.get("fromDate"));
		incomeReq.put("toDate", range.get("toDate"));

		HashMap<String, Object> expenseReq = new HashMap<>();
		expenseReq.put("userId", myChall.getUserId());
		expenseReq.put("fromDate", range.get("fromDate"));
		expenseReq.put("toDate", range.get("toDate"));

		HashMap<String, Object> income = dao.getIncomeSummaryByRange(sqlSession, incomeReq);
		HashMap<String, Object> expense = dao.getExpenseSummaryByRange(sqlSession, expenseReq);

		int incomeSum = toInt(income.get("sum"));
		int incomeCnt = toInt(income.get("cnt"));

		int expenseSum = toInt(expense.get("sum"));
		int expenseCnt = toInt(expense.get("cnt"));

		// 수입/지출이 "각각" 최소 1건 이상 있어야 참여 가능
		if (incomeCnt < 1 || expenseCnt < 1) {
			throw new IllegalArgumentException(
					"챌린지 참여를 위해서는 해당 월에 수입/지출 내역이 각각 1건 이상 필요합니다. "
							+ "(검증 기간: " + range.get("fromDate") + " ~ " + range.get("toDate") + ")"
			);
		}

		// 3) 잔액 계산 (수입 - 지출)
		int balance = incomeSum - expenseSum;

		// 4) 챌린지별 필요 잔액 계산
		int required = calcRequiredBalance(challenge);

		// 5) 잔액 조건
		if (balance < required) {
			throw new IllegalArgumentException(
					"잔액이 부족하여 챌린지에 참여할 수 없습니다. "
							+ "(현재 잔액: " + balance + "원, 필요 잔액: " + required + "원)"
							+ " / (검증 기간: " + range.get("fromDate") + " ~ " + range.get("toDate") + ")"
			);
		}
		
		// 2월 1일 12시 55분 추가 
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        start = toLocalDate(myChall.getStartDate());

        if (start.isEqual(today)) {
            myChall.setStatus("PROCEEDING"); // 오늘이면 즉시 진행중
        } else {
            myChall.setStatus("RESERVED");   // 미래면 예약중
        }
		  
		 
		 

		// 6) 조건 통과 -> 참여 저장 (STATUS=RESERVED로 들어감, 스케줄러가 시작일에 PROCEEDING으로 바꿈)
		return dao.joinMyChallenge(sqlSession, myChall);
	}

	
	// - "챌린지 시작일이 속한 달" 기준으로, 시작일 **전날까지**(포함)만 집계함
	// - startDate가 그 달 1일이면: 이전 달 1일 ~ 이전 달 말일 (검증 기간이 0일이 되는 걸 방지)
	private Map<String, String> calcCheckRange(java.time.LocalDate startDate) {
		java.time.LocalDate s = startDate;
		if (s == null) s = java.time.LocalDate.now();

		java.time.LocalDate monthStart = s.withDayOfMonth(1);

		// startDate가 1일인 경우: 이전 달 전체로 체크
		if (s.getDayOfMonth() == 1) {
			java.time.LocalDate prevMonthEnd = monthStart.minusDays(1);
			java.time.LocalDate prevMonthStart = prevMonthEnd.withDayOfMonth(1);
			return Map.of(
				"fromDate", prevMonthStart.toString(),
				"toDate", prevMonthEnd.toString()
			);
		}

		// 같은 달 안에서 monthStart ~ (startDate - 1일)
		java.time.LocalDate end = s.minusDays(1);
		return Map.of(
			"fromDate", monthStart.toString(),
			"toDate", end.toString()
		);
	}

	// [ADDED] 챌린지별 "필요 잔액" 계산
	// - 쇼핑: 200,000 이상(요구사항 하드코딩)
	// - 하루당 제한형(예: 10,000원 이하 3일): target * duration
	// - 그 외 target 있는 경우: target
	private int calcRequiredBalance(Challenge ch) {
		if (ch == null) return 0;

		// ✅ 쇼핑(요구사항): 잔액 20만원 이상일 때만 참여 가능
		if ("쇼핑".equals(ch.getCategory()) || "impulse_control_challenge".equals(ch.getChallengeId())) {
			return 200_000;
		}

		int target = ch.getTarget();       // 없으면 0
		int duration = ch.getDuration();   // 없으면 0

		// "하루 당 ~" 타입은 보통 duration이 의미가 있음
		// 10,000원 이하로 3일 -> 최소 30,000원은 있어야 너무 쉬운 참여가 안 됨
		if (target > 0 && duration > 0) {
			return target * duration;
		}

		// target만 있는 경우
		if (target > 0) return target;

		return 0;
	}

	private int toInt(Object v) {
		if (v == null) return 0;
		if (v instanceof Number) return ((Number) v).intValue();
		try {
			return Integer.parseInt(String.valueOf(v));
		} catch (Exception e) {
			return 0;
		}
	}

	private LocalDate toLocalDate(java.util.Date d) {
		if (d == null) return null;
		return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	
	// 1) RESERVED -> PROCEEDING (시작일 도래)
	public int promoteReservedToProceeding() {
		return dao.promoteReservedToProceeding(sqlSession);
	}

	// 2) PROCEEDING 중인데 종료일이 지난 것들을 SUCCESS/FAILED로 판정해서 마감
	// - 성공 판정은 "챌린지 종류별 조건"으로 계산함
	public int closeExpiredProceedingToResult() {
		ArrayList<MyChallHistory> endedList = dao.selectEndedProceedingChallenges(sqlSession);
		if (endedList == null || endedList.isEmpty()) return 0;

		int updated = 0;
		for (MyChallHistory h : endedList) {
			boolean success = evaluateSuccess(h);
			HashMap<String, Object> param = new HashMap<>();
			param.put("userId", h.getUserId());
			param.put("challengeId", h.getChallengeId());
			param.put("startDate", h.getStartDate());
			param.put("endDate", h.getEndDate());
			param.put("status", success ? "SUCCESS" : "FAILED");

			updated += dao.updateMyChallStatus(sqlSession, param);
		}
		return updated;
	}

	// -------------------------
	// 성공 판정 로직 (MYTRANS 기반)
	// -------------------------
	private boolean evaluateSuccess(MyChallHistory h) {
	    if (h == null) return false;

	    final String challengeId = h.getChallengeId() == null ? "" : h.getChallengeId();
	    final String desc = h.getDescription() == null ? "" : h.getDescription();
	    final String category = h.getCategory();

	    final int target = h.getTarget();
	    final int duration = Math.max(1, h.getDuration());
	    final int targetCount = h.getTargetCount();

	    String fromDate = toIsoDate(h.getStartDate());
	    String toDate = toIsoDate(h.getEndDate());
	    if (fromDate == null || toDate == null) return false;

	    // 1) 횟수 제한형 (예: 쇼핑 2회 이하)
	    if (targetCount > 0) {
	        HashMap<String, Object> p = new HashMap<>();
	        p.put("userId", h.getUserId());
	        p.put("fromDate", fromDate);
	        p.put("toDate", toDate);
	        p.put("category", category);
	        
	        Map<String, Object> resultMap = dao.getExpenseCountByRange(sqlSession, p);
	        
	        // 키값(CNT/cnt) 무시하고 숫자 데이터 추출
	        int cnt = 0;
	        for (Object value : resultMap.values()) {
	            if (value instanceof Number) {
	                cnt = ((Number) value).intValue();
	                break;
	            }
	        }
	        return cnt <= targetCount; // 목표 횟수 이하로 썼으면 성공
	    }

	    // 2) "하루당" 일일 제한형 (예: 하루 10,000원 이하로 3일)
	    boolean looksDaily = desc.contains("하루") || challengeId.contains("3days") || challengeId.contains("daily");
	    if (looksDaily && target > 0) {
	        HashMap<String, Object> p = new HashMap<>();
	        p.put("userId", h.getUserId());
	        p.put("fromDate", fromDate);
	        p.put("toDate", toDate);
	        p.put("category", category);

	        ArrayList<HashMap<String, Object>> rows = dao.getDailyExpenseSumsByRange(sqlSession, p);
	        Map<String, Integer> sumByDay = new HashMap<>();
	        
	        if (rows != null) {
	            for (HashMap<String, Object> r : rows) {
	                // 날짜 키값 처리 (DAY/day)
	                Object dayObj = r.get("DAY") != null ? r.get("DAY") : r.get("day");
	                String day = String.valueOf(dayObj);
	                
	                // 금액 숫자 데이터 추출
	                int daySum = 0;
	                for (Object value : r.values()) {
	                    if (value instanceof Number) {
	                        daySum = ((Number) value).intValue();
	                        break;
	                    }
	                }
	                sumByDay.put(day, daySum);
	            }
	        }

	        LocalDate s = toLocalDate(fromDate);
	        LocalDate e = toLocalDate(toDate);
	        if (s == null || e == null) return false;

	        // 시작일부터 종료일까지 하루라도 목표액을 넘었는지 전수 조사
	        for (LocalDate cur = s; !cur.isAfter(e); cur = cur.plusDays(1)) {
	            int sum = sumByDay.getOrDefault(cur.toString(), 0);
	            if (sum > target) return false; // 하루라도 초과하면 실패
	        }
	        return true; 
	    }

	    // 3) 총액 제한형 (예: 식비 50,000원 이하 유지)
	    if (target > 0) {
	        HashMap<String, Object> p = new HashMap<>();
	        p.put("userId", h.getUserId());
	        p.put("fromDate", fromDate);
	        p.put("toDate", toDate);
	        p.put("category", category);
	        
	        Map<String, Object> resultMap = dao.getExpenseSumByRange(sqlSession, p);
	        int totalSum = 0;
	        for (Object value : resultMap.values()) {
	            if (value instanceof Number) {
	                totalSum = ((Number) value).intValue();
	                break;
	            }
	        }
	        return totalSum <= target; // 총액이 목표 이하이면 성공
	    }

	    return true; // 기준이 없는 경우 성공 처리
	}

	private String toIsoDate(Object v) {
		LocalDate d = toLocalDate(v);
		return d == null ? null : d.toString();
	}

	private LocalDate toLocalDate(Object v) {
		if (v == null) return null;
		if (v instanceof LocalDate) return (LocalDate) v;
		if (v instanceof Date) return ((Date) v).toLocalDate();
		if (v instanceof java.util.Date) return ((java.util.Date) v).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		if (v instanceof String) {
			try { return LocalDate.parse((String) v); } catch(Exception e) { return null; }
		}
		return null;
	}

	// 1.5) PROCEEDING 중인데 이미 조건을 위반한 챌린지는 즉시 FAILED 처리 (기간 끝까지 기다리지 않음)
	// - 예: 쇼핑 횟수 2회 이하인데 3회 이상 지출하면 바로 FAILED
	public int closeViolatedProceedingToFailed() {
	    ArrayList<MyChallHistory> activeList = dao.selectActiveProceedingChallenges(sqlSession);
	    if (activeList == null || activeList.isEmpty()) return 0;

	    int updated = 0;
	    for (MyChallHistory h : activeList) {
	        boolean violated = evaluateViolationNow(h);
	        if (!violated) continue;

	        HashMap<String, Object> param = new HashMap<>();
	        param.put("userId", h.getUserId());
	        param.put("challengeId", h.getChallengeId());
	        param.put("startDate", h.getStartDate());
	        param.put("endDate", h.getEndDate());
	        param.put("status", "FAILED");

	        updated += dao.updateMyChallStatus(sqlSession, param);
	    }
	    return updated;
	}

	private boolean evaluateViolationNow(MyChallHistory h) {
	    if (h == null) return false;

	    final String challengeId = h.getChallengeId() == null ? "" : h.getChallengeId();
	    final String desc = h.getDescription() == null ? "" : h.getDescription();
	    final String category = h.getCategory();
	    final int target = h.getTarget();
	    final int targetCount = h.getTargetCount();

	    String fromDate = toIsoDate(h.getStartDate());
	    String toDate = toIsoDate(LocalDate.now(ZoneId.systemDefault())); 
	    if (fromDate == null || toDate == null) return false;

	    // 1) 횟수 제한형 (쇼핑 등) - 기존 로직 유지
	    if (targetCount > 0) {
	        HashMap<String, Object> p = new HashMap<>();
	        p.put("userId", h.getUserId());
	        p.put("fromDate", fromDate);
	        p.put("toDate", toDate);
	        p.put("category", category);

	        Map<String, Object> resultMap = dao.getExpenseCountByRange(sqlSession, p);
	        int cnt = 0;
	        if (resultMap != null) {
	            for (Object v : resultMap.values()) {
	                if (v instanceof Number) {
	                    cnt = ((Number) v).intValue();
	                    break;
	                }
	            }
	        }
	        if (cnt > targetCount) return true;
	    }

	    // 2) 일일 제한형 (교통 10,000원 이하 등) - ✅ 이 부분을 확실하게 수정
	    boolean looksDaily = desc.contains("하루") || challengeId.contains("3days") || challengeId.contains("daily");
	    if (looksDaily && target > 0) {
	        HashMap<String, Object> p = new HashMap<>();
	        p.put("userId", h.getUserId());
	        p.put("fromDate", fromDate);
	        p.put("toDate", toDate);
	        p.put("category", category);

	        // DB에서 일자별 합계를 가져옴
	        ArrayList<HashMap<String, Object>> rows = dao.getDailyExpenseSumsByRange(sqlSession, p);
	        if (rows != null) {
	            for (HashMap<String, Object> r : rows) {
	                int daySum = 0;
	                // DB가 SUM, sum, CNT, cnt 중 무엇으로 반환해도 대응 가능합니다.
	                for (Object v : r.values()) {
	                    if (v instanceof Number) {
	                        daySum = ((Number) v).intValue();
	                        
	                        if (daySum > target) return true; 
	                    }
	                }
	            }
	        }
	        return false;
	    }

	    // 3) 총액 제한형 - 기존 로직 유지
	    if (target > 0) {
	        HashMap<String, Object> p = new HashMap<>();
	        p.put("userId", h.getUserId());
	        p.put("fromDate", fromDate);
	        p.put("toDate", toDate);
	        p.put("category", category);

	        Map<String, Object> resultMap = dao.getExpenseSumByRange(sqlSession, p);
	        int totalSum = 0;
	        if (resultMap != null) {
	            for (Object v : resultMap.values()) {
	                if (v instanceof Number) {
	                    totalSum = ((Number) v).intValue();
	                    break;
	                }
	            }
	        }
	        if (totalSum > target) return true;
	    }
	    return false;
	}
	
	//그룹 챌린지

	 // =========================
    // 1) 그룹 챌린지 참여
    // =========================
	
    @Override
    @Transactional
    public int joinGroupChallenge(GroupChall groupChall) {

        // 1) 기존 로직: GROUPCHALL insert
        int result = dao.joinGroupChallenge(sqlSession, groupChall); 
        // ↑ 이 메서드는 네 기존 dao에 이미 있을 가능성이 큼 (없으면 추가)

        // 2) ✅ 추가: 결과 테이블에 그룹원 전원 PROCEEDING 생성
        // groupChall 안에 startDate/endDate가 들어있어야 함
        Map<String, Object> p = new HashMap<>();
        p.put("groupbId", groupChall.getGroupbId());
        p.put("challengeId", groupChall.getChallengeId());
        p.put("startDate", groupChall.getStartDate());
        p.put("endDate", groupChall.getEndDate());

        dao.insertGroupChallResults(sqlSession, p);

        return result;
    }

    // =========================
    // 2) 무지출 즉시 탈락 처리 (지출 저장 시 호출)
    // =========================
    @Override
    @Transactional
    public int handleZeroChallengeExpense(int groupbId, int userId, Date transDate) {

        Map<String, Object> p = new HashMap<>();
        p.put("groupbId", groupbId);
        p.put("userId", userId);
        p.put("transDate", transDate);

        // 결과 테이블에서 해당 유저를 즉시 FAILED로
        return dao.failUserOnZeroChallengeExpense(sqlSession, p);
    }

    // =========================
    // 3) 스케줄러: 종료 처리 + 결과 확정 + 뱃지 발급
    // =========================
    @Override
    @Transactional
    public void runGroupChallengeScheduler() {

    	dao.closeExpiredGroupChallenges(sqlSession);
        // (A) 무지출: 기간 종료된 PROCEEDING 유저 SUCCESS 처리
        dao.successRemainingZeroChallengeUsers(sqlSession);

        // (B) 경쟁형: 종료된 competition 회차들 처리
        List<Map<String, Object>> endedCompetitions = dao.selectEndedCompetitionChallenges(sqlSession);

        for (Map<String, Object> row : endedCompetitions) {
            // row에 groupbId, challengeId, startDate, endDate가 들어있어야 함
            // (mapper selectEndedCompetitionChallenges 결과 컬럼에 맞추기)

            dao.updateCompetitionTotals(sqlSession, row);
            dao.updateCompetitionRanks(sqlSession, row);
            dao.finalizeCompetitionStatus(sqlSession, row);

             //(선택) GROUPCHALL 자체 상태도 CLOSED/SUCCESS로 닫고 싶다면
            row.put("status", "SUCCESS");
            dao.closeGroupChallenge(sqlSession, row);
        }

        // (C) SUCCESS인 사람만 뱃지 지급(중복방지는 mapper에서 NOT EXISTS나 MERGE로 처리)
        List<Map<String, Object>> rewardList = dao.selectUsersToRewardFromResult(sqlSession);

        for (Map<String, Object> r : rewardList) {
            // r: USER_ID, BADGE_ID 형태
            dao.mergeUserBadge(sqlSession, r);
        }
    }
	

	@Override
	public List<GroupChall> getGroupJoinList(int groupbId) {
		return dao.getGroupJoinList(sqlSession, groupbId);
	}
//	
//
//	@Override
//	public int failActiveZeroChallenge(int groupbId) {
//	    return dao.failActiveZeroChallenge(sqlSession, groupbId);
//	}
//	
	public List<Map<String, Object>> getGroupRanking(int groupbId, String challengeId) {
	    Map<String, Object> params = new HashMap<>();
	    params.put("groupbId", groupbId);
	    params.put("challengeId", challengeId);
	    return dao.getGroupRanking(sqlSession, params);
	}
//
	@Override
	public List<GroupChall> getGroupPastChallengeList(int groupbId) {
		return dao.getGroupPastChallengeList(sqlSession, groupbId);
	}
//	
//	@Transactional
//	@Override
//	public void closeExpiredChallenges() {
//	    List<Map<String, Object>> rewardList = dao.getUsersToReward(sqlSession); 
//
//	    System.out.println("rewardList = " + rewardList);
//	    if(rewardList != null && !rewardList.isEmpty()){
//	        System.out.println("rewardList[0].keys = " + rewardList.get(0).keySet());
//	    
//	        
//	        for (Map<String, Object> reward : rewardList) {
//	            Object uIdObj = reward.get("userId") != null ? reward.get("userId") : reward.get("USER_ID");
//	            Object bIdObj = reward.get("badgeId") != null ? reward.get("badgeId") : reward.get("BADGE_ID");
//
//	            if (uIdObj != null && bIdObj != null) {
//	                int userId = Integer.parseInt(String.valueOf(uIdObj));
//	                int badgeId = Integer.parseInt(String.valueOf(bIdObj));
//	                
//	                int ins = badgeService.insertDefaultBadge(userId, badgeId);
//	                System.out.println("USERBADGE insert result = " + ins + " (userId=" + userId + ", badgeId=" + badgeId + ")");
//	            }
//	        }
//	    }
//	    int result = dao.updateGroupChallengeSuccess(sqlSession);
//	    System.out.println("챌린지 종료 처리 완료: " + result + "건");
//	}
	
//	@Transactional
//	public void checkAndRewardChallenges() {
//	    // 1. 성공 상태로 업데이트하기 전, 조건에 맞는 유저 ID 리스트 확보 (쿼리 추가 필요)
//	    List<Integer> successUserIds = dao.getUsersToReward(sqlSession); 
//
//	    // 2. 챌린지 상태를 SUCCESS로 업데이트
//	    int updatedCount = dao.updateGroupChallengeSuccess(sqlSession);
//
//	    // 3. 업데이트된 유저들에게 뱃지 지급
//	    if (updatedCount > 0 && successUserIds != null) {
//	        for (int userId : successUserIds) {
//	            // 해당 성공 챌린지에 맞는 특정 badgeId를 부여 (예: 2번 뱃지)
//	            badgeService.insertDefaultBadge(userId, badgeId); 
//	        }
//	    }
//	}

}








