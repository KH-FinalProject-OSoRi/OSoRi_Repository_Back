package com.kh.osori.groupBudgetMem.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.osori.groupBudgetMem.model.vo.GroupBudgetMem;
import com.kh.osori.groupBudgetMem.service.GroupBudgetMemService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/groupMem")
@CrossOrigin
public class GroupBudgetMemController {
	
	@Autowired
	private GroupBudgetMemService service;
	
	@GetMapping("/searchGroupMem/{groupId}")
	public ResponseEntity<?> searchGroupMem(@PathVariable int groupId){
		List<GroupBudgetMem> list = service.searchGroupMem(groupId);
        
        return ResponseEntity.ok(list);
    }
}
	
	
	
	


