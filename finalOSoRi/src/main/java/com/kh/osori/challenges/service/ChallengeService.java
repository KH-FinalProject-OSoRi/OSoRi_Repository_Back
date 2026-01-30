package com.kh.osori.challenges.service;

import java.util.ArrayList;

import com.kh.osori.challenges.model.vo.Challenge;

public interface ChallengeService {
	
	ArrayList<Challenge> getChallengeList(String challengeMode); 

}
