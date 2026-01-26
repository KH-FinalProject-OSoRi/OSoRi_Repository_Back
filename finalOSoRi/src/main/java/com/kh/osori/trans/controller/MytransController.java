package com.kh.osori.trans.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.osori.trans.model.vo.Mytrans;
import com.kh.osori.trans.service.MytransServiceImpl;
import com.kh.osori.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/trans")
@CrossOrigin
public class MytransController {
	
	@Autowired
	private MytransServiceImpl service;
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/myTransSave")
	public ResponseEntity<?> myTransSave(@RequestBody Mytrans mt){
		
		
		mt.setIsShared("N");  
	    mt.setGroupTransId(null);
	    
	    if ("수입".equals(mt.getType())) {
	        mt.setType("IN");
	    } else if ("지출".equals(mt.getType())) {
	        mt.setType("OUT");
	    }

		System.out.println(mt);
		
		int result = service.myTransSave(mt);
		
		if(result>0) {
			return ResponseEntity.status(HttpStatus.CREATED)
								 .body("거래내역 등록성공!");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
								 .body("거래내역 등록실패");
		}
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getMyTransactions(@PathVariable int userId) {
	    log.info("내 거래내역 조회 userId={}", userId);

	    return ResponseEntity.ok(service.getMyTransactions(userId));
	}


}
