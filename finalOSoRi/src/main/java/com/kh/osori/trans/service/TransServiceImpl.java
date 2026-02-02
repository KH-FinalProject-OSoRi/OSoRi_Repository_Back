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

	public int myTransSave(Mytrans mt) {	
		return dao.myTransSave(sqlSession,mt);
				
	}

	@Transactional
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
	
	public List<Mytrans> getMyTransactions(int userId) {
		return dao.selectMyTrans(sqlSession, userId);
	}

	public int updateTrans(Mytrans mt) {	
		return dao.updateTrans(sqlSession,mt);
	}

	public int deleteTrans(int transId) {
		
		return dao.deleteTrans(sqlSession,transId);
	}
	
	// [추가] 고정지출 -> MYTRANS 자동반영 MERGE 실행
	public int mergeFixedToMyTrans() {
	  return dao.mergeFixedToMyTrans(sqlSession);
	}


	public List<Grouptrans> getGroupTransactions(int groupId) {
		return dao.getGroupTransactions(sqlSession,groupId);
	}
	
	public int updateGroupTrans(Grouptrans gt) {
			
			return dao.updateGroupTrans(sqlSession,gt);
		}

	public int deleteGroupTrans(int transId) {
		
		return dao.deleteGroupTrans(sqlSession,transId);
	}

	public Object groupInfo(int groupId) {
		
		return dao.groupInfo(sqlSession,groupId);
	}

	public List<Mytrans> recentTrans(int userId) {
		
		return dao.recentTrans(sqlSession,userId);
	}




}
