package com.kh.osori.challenges.model.service;

import java.util.ArrayList;

import com.kh.osori.challenges.model.vo.Challenges;

public interface ChallengeService {
	
	ArrayList<Challenges> getChallengeList(String challengeMode); 

}
