package com.kh.osori.trans.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.osori.trans.model.vo.Grouptrans;
import com.kh.osori.trans.model.vo.Mytrans;
import com.kh.osori.trans.service.TransServiceImpl;
import com.kh.osori.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/trans")
@CrossOrigin
public class TransController {

	@Autowired
	private TransServiceImpl service;
	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/myTransSave")
	public ResponseEntity<?> myTransSave(@RequestBody Mytrans mt) {

		if (mt.getIsShared() == null) {
	        mt.setIsShared("N");
	    }

		int result = service.myTransSave(mt);

		if (result > 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body("거래내역 등록성공!");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("거래내역 등록실패");
		}
	}

	@PostMapping("/groupTransSave")
	public ResponseEntity<?> groupTransSave(@RequestBody Grouptrans gt) {

		int result = service.GroupTransSave(gt);

		if (result > 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body("거래내역 등록성공!");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("거래내역 등록실패");
		}
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getMyTransactions(@PathVariable int userId) {

		return ResponseEntity.ok(service.getMyTransactions(userId));
	}
	
	@GetMapping("/groupTransList/{groupId}")
	public ResponseEntity<?> getGroupTransactions(@PathVariable int groupId) {

		return ResponseEntity.ok(service.getGroupTransactions(groupId));
	}

	@PutMapping("/updateTrans")
	public ResponseEntity<?> updateTrans(@RequestBody Mytrans mt) {

		int result = service.updateTrans(mt);

		if (result > 0) {
			return ResponseEntity.ok("게시글 수정 성공");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 정보 수정 실패!");
		}
	}

	@DeleteMapping("/deleteTrans/{transId}")
	public ResponseEntity<?> deleteTrans(@PathVariable int transId) {

		int result = service.deleteTrans(transId);

		if (result > 0) {
			return ResponseEntity.ok("게시글 삭제 성공");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 삭제 실패!");
		}

	}
	
	@PutMapping("/updateGroupTrans")
	public ResponseEntity<?> updateGroupTrans(@RequestBody Grouptrans gt) {

		int result = service.updateGroupTrans(gt);

		if (result > 0) {
			return ResponseEntity.ok("게시글 수정 성공");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 정보 수정 실패!");
		}
	}

	@DeleteMapping("/deleteGroupTrans/{transId}")
	public ResponseEntity<?> deleteGroupTrans(@PathVariable int transId) {

		int result = service.deleteGroupTrans(transId);

		if (result > 0) {
			return ResponseEntity.ok("게시글 삭제 성공");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 삭제 실패!");
		}
	}
	
	@GetMapping("/groupInfo/{groupId}")
	public ResponseEntity<?> groupInfo(@PathVariable int groupId){
		return ResponseEntity.ok(service.groupInfo(groupId));
	}
	
	@GetMapping("/recentTrans/{userId}")
	public ResponseEntity<?> recentTrans(@PathVariable int userId){
		return ResponseEntity.ok(service.recentTrans(userId));
	}
	
}
