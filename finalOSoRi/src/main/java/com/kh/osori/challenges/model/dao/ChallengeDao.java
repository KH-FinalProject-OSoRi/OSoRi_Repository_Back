package com.kh.osori.challenges.model.dao;

import java.util.ArrayList;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.osori.challenges.model.vo.Challenges;

@Repository
public class ChallengeDao {

	public ArrayList<Challenges> getChallengeList(SqlSessionTemplate sqlSession, String challengeMode) {
		return (ArrayList) sqlSession.selectList("challengeMapper.getChallengeList", challengeMode);
	}

}
