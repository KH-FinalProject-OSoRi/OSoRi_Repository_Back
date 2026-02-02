package com.kh.osori.challenges.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.osori.challenges.model.vo.Challenges;
import com.kh.osori.challenges.model.service.ChallengeService;

@RestController
@RequestMapping("/challenges")
public class ChallengeController {
	
	@Autowired
	private ChallengeService service; 
	
	@GetMapping
	public ResponseEntity<?> getChallengeList(@RequestParam String challengeMode) {
		
		ArrayList<Challenges> list = service.getChallengeList(challengeMode); 
		
		HashMap<String, Object> res = new HashMap<>(); 
		
		if(list != null) {
			return ResponseEntity.ok(list); 
		} else {
			res.put("message", "챌린지 목록을 조회 할 수 없습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
		
	}

}
