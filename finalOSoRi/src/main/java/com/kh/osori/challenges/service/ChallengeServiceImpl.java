package com.kh.osori.challenges.service;

import java.util.ArrayList;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.osori.challenges.model.dao.ChallengeDao;

import com.kh.osori.challenges.model.vo.Challenge;

@RestController
@RequestMapping("/challenges")
public class ChallengeServiceImpl implements ChallengeService {
	
	@Autowired
	private ChallengeDao dao; 
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Override
	public ArrayList<Challenge> getChallengeList(String challengeMode) {
		
		ArrayList<Challenge> list = dao.getChallengeList(sqlSession, challengeMode);
		
		return list; 
		
	}
	
	
	

}
