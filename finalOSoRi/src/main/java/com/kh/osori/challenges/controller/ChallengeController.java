package com.kh.osori.challenges.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.osori.challenges.model.dto.JoinGroupChallengeRequest;
import com.kh.osori.challenges.model.dto.JoinMyChallengeRequest;
import com.kh.osori.challenges.model.vo.Challenge;
import com.kh.osori.challenges.model.vo.GroupChall;
import com.kh.osori.challenges.model.vo.MyChall;
import com.kh.osori.challenges.model.vo.MyChallHistory;
import com.kh.osori.challenges.service.ChallengeService;

@RestController
@RequestMapping("/challenges")
public class ChallengeController {
	
	@Autowired
	private ChallengeService service; 
	
	@GetMapping
	public ResponseEntity<?> getChallengeList(@RequestParam String challengeMode) {

		ArrayList<Challenge> list = service.getChallengeList(challengeMode);

		HashMap<String, Object> res = new HashMap<>();

		if (list != null) {
			return ResponseEntity.ok(list);
		} else {
			res.put("message", "챌린지 목록을 조회 할 수 없습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
	}

	// 참여하기 (잔액 검증 포함)
	@PostMapping("/mychallenges")
	public ResponseEntity<?> joinMyChallenge(@RequestBody JoinMyChallengeRequest req) {

		HashMap<String, Object> res = new HashMap<>();

	    try {
	        MyChall myChall = MyChall.builder()
	                .challengeId(req.getChallengeId())
	                .userId(req.getUserId())
	                .startDate(req.getStartDate())
	                .endDate(req.getEndDate())
	                .build();

	        int result = service.joinMyChallengeWithBalanceCheck(
	                myChall,
	                req.getExpenseSum(),
	                req.getExpenseCount()
	        );

	        if (result > 0) {
	            return ResponseEntity.ok("챌린지 등록 성공했습니다.");
	        } else {
	            res.put("message", "서버에 문제가 생겨서 챌린지 등록을 실패했습니다.");
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
	        }

	    } catch (IllegalArgumentException e) {
	        // 검증 실패는 400 + 메시지로 내리기
	        res.put("message", e.getMessage());
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);

	    } catch (IllegalStateException e) {
	        res.put("message", e.getMessage());
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);

	    } catch (Exception e) {
	        res.put("message", "서버에 문제가 생겨서 챌린지 등록을 실패했습니다.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
	    }
	}

	// 참여 목록 조회
	@GetMapping("/mychallenges")
	public ResponseEntity<?> getMyChallengeList(@RequestParam int userId,
			@RequestParam(required = false) String challengeMode) {

		HashMap<String, Object> req = new HashMap<>();

		req.put("userId", userId);
		req.put("challengeMode", challengeMode);

		ArrayList<MyChall> list = service.getMyChallengeList(req);

		HashMap<String, Object> res = new HashMap<>();

		if (list != null) {
			return ResponseEntity.ok(list);
		} else {
			res.put("message", "내 챌린지 목록을 조회 할 수 없습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
	}

	// 지난 챌린지 (END_DATE 지난 것만 / SUCCESS, FAILED)
	@GetMapping("/mychallenges/past")
	public ResponseEntity<?> getMyPastChallengeList(@RequestParam int userId,
			@RequestParam(required = false) String challengeMode) {

		HashMap<String, Object> req = new HashMap<>();
		req.put("userId", userId);
		req.put("challengeMode", challengeMode);

		ArrayList<MyChallHistory> list = service.getMyPastChallengeList(req);

		HashMap<String, Object> res = new HashMap<>();
		if (list != null) {
			return ResponseEntity.ok(list);
		} else {
			res.put("message", "지난 챌린지 목록을 조회 할 수 없습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
	}
	
	//그룹챌린지
	
	@PostMapping("/group")
    public ResponseEntity<?> joinGroupChallenge(@RequestBody JoinGroupChallengeRequest req) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime calculatedEndDate = now.plusDays(req.getDuration());

        GroupChall groupChall = GroupChall.builder()
                .challengeId(req.getChallengeId())
                .groupbId(req.getGroupbId())
                .startDate(now)   
                .endDate(calculatedEndDate)      
                .status("PROCEEDING")
                .build();

        // 2. 서비스에서 GROUPCHALL INSERT + GROUPCHALL_RESULT 생성 (대공사 로직 포함)
        // 서비스 내부에서 @Transactional 처리가 되어 있어야 두 인서트가 안전하게 묶입니다.
        int result = service.joinGroupChallenge(groupChall);

        if (result > 0) {
            return ResponseEntity.ok(groupChall);
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("그룹 챌린지 참여 처리 중 오류가 발생했습니다.");
        }
    }

    // 순위 조회 - ServiceImpl에서 작성한 RNK 컬럼 기반 데이터를 반환합니다.
    @GetMapping("/group/ranking")
    public ResponseEntity<?> getGroupRanking(
            @RequestParam("groupbId") int groupbId,
            @RequestParam("challengeId") String challengeId) {
        
        // 매퍼의 updateCompetitionRanks 등을 통해 갱신된 최신 순위를 가져옵니다.
        List<Map<String, Object>> rankingList = service.getGroupRanking(groupbId, challengeId);
        
        if (rankingList != null) {
            return ResponseEntity.ok(rankingList);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("순위 정보를 찾을 수 없습니다.");
        }
    }

    @GetMapping("/groupJoinedList")
    public ResponseEntity<?> getGroupJoinedList(@RequestParam("groupbId") int groupbId , @RequestParam("userId") int userId) {
        return ResponseEntity.ok(service.getGroupJoinedList(groupbId, userId));
    }

    @GetMapping("/group/past")
    public ResponseEntity<?> getGroupPastChallengeList(@RequestParam("groupbId") int groupbId, @RequestParam("userId") int userId) {
        List<Map<String, Object>> list = service.getGroupPastChallengeList(groupbId, userId);
        if (list != null) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("message", "지난 목록 조회 실패"));
        }
    }
	
	// [ADDED] 개인 챌린지 실시간 진행도 조회 API
    @GetMapping("/mychallenges/progress")
    public ResponseEntity<?> getChallengeProgress(@RequestParam int userId, @RequestParam String challengeId) {
        Map<String, Object> progress = service.getChallengeProgress(userId, challengeId);
        if (progress != null) {
            return ResponseEntity.ok(progress);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("진행 중인 챌린지 데이터를 찾을 수 없습니다.");
    }
	
	
	

}
