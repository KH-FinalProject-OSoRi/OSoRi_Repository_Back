package com.kh.osori.faq.model.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.osori.faq.model.service.FaqService;
import com.kh.osori.faq.model.vo.Faq;

@RestController
@RequestMapping("/faq")
public class FaqController {

	@Autowired
	private FaqService service;
	
	@GetMapping("/questionList")
	public ResponseEntity<?> questionList(){
		List<Faq> qList = service.questionList();

		if(qList != null && !qList.isEmpty()) {
			return ResponseEntity.ok(qList);
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("질문 목록 조회를 실패했습니다.");
		}
	}
	
	@PostMapping("/addNewQuestion")
	public ResponseEntity<?> addNewQuestion(@RequestBody String question){
		int result = service.addNewQuestion(question);
		
		if(result > 0) {
			return ResponseEntity.ok(200);
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("질문 목록 조회를 실패했습니다.");
		}
	}
	
}
