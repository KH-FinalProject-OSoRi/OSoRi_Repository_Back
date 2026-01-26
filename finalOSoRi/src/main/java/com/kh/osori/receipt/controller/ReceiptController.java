package com.kh.osori.receipt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.osori.receipt.model.vo.ReceiptDTO;
import com.kh.osori.receipt.service.ReceiptService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ReceiptController {
	
	@Autowired
	private ReceiptService service;
	
	@PostMapping("/ocr")
	public ResponseEntity<?> api(@RequestParam("receipt") MultipartFile file){
		
		ReceiptDTO result = service.processReceipt(file);
		
		if(result != null) {
			return ResponseEntity.ok(result);
		} else{
			return ResponseEntity.internalServerError().build();
		}
		
	}

}
