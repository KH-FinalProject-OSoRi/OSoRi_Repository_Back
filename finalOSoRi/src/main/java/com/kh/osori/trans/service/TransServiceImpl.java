package com.kh.osori.trans.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.osori.challenges.service.ChallengeService;
import com.kh.osori.trans.dao.TransDao;
import com.kh.osori.trans.model.vo.Grouptrans;
import com.kh.osori.trans.model.vo.Mytrans;

@Service
public class TransServiceImpl implements TransService{
	
	@Autowired
	private TransDao dao;
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	//추가
	@Autowired
	private ChallengeService challengeService;


	@Override
	public int myTransSave(Mytrans mt) {
		
		return dao.myTransSave(sqlSession,mt);
				
	}
	
	@Override
	public int GroupTransSave(Grouptrans gt) {
        // 1. 기존 거래 내역 저장 로직 실행
        int result = dao.groupTransSave(sqlSession, gt);

        // 2. 저장 성공(result > 0) 및 지출(OUT) 타입인지 확인
        if (result > 0 && "OUT".equalsIgnoreCase(gt.getTransType())) {
            // 3. Challenge 서비스의 실패 처리 메서드 호출
            // gt.getGroupbId()를 통해 현재 가계부의 챌린지만 골라 실패 처리함
        	System.out.println("지출 감지! 챌린지 실패 로직 실행. 가계부ID: " + gt.getGroupBId());
            challengeService.failActiveZeroChallenge(gt.getGroupBId());
        }

        return result;
    }
	
	@Override
	public List<Mytrans> getMyTransactions(int userId) {
		return dao.selectMyTrans(sqlSession, userId);
	}

	@Override
	public int updateTrans(Mytrans mt) {
		
		return dao.updateTrans(sqlSession,mt);
	}

	@Override
	public int deleteTrans(int transId) {
		
		return dao.deleteTrans(sqlSession,transId);
	}
	
	// [추가] 고정지출 -> MYTRANS 자동반영 MERGE 실행
	public int mergeFixedToMyTrans() {
	  return dao.mergeFixedToMyTrans(sqlSession);
	}


	@Override
	public List<Grouptrans> getGroupTransactions(int groupId) {
		return dao.getGroupTransactions(sqlSession,groupId);
	}
	
	@Override
	public int updateGroupTrans(Grouptrans gt) {
			
			return dao.updateGroupTrans(sqlSession,gt);
		}

	@Override
	public int deleteGroupTrans(int transId) {
		
		return dao.deleteGroupTrans(sqlSession,transId);
	}

	@Override
	public Object groupInfo(int groupId) {
		
		return dao.groupInfo(sqlSession,groupId);
	}

	@Override
	public List<String> getGroupMemberIds(int groupBId) {
		return dao.getGroupMemberIds(sqlSession,groupBId);
	}
	
	public List<Mytrans> recentTrans(int userId) {
		
		return dao.recentTrans(sqlSession,userId);
	}

	@Override
	public String getLoginId(int userId) {
		return dao.getLoginId(sqlSession, userId);
	}

	public String getGroupTitle(int groupBId) {
		return dao.getGroupTitle(sqlSession, groupBId);
	}

}
