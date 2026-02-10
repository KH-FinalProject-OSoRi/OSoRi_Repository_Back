package com.kh.osori.groupBudget.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.osori.groupBudget.model.service.GroupBudgetService;
import com.kh.osori.groupBudget.model.vo.BudgetMem;
import com.kh.osori.groupBudget.model.vo.GroupBudget;
import com.kh.osori.notification.model.service.NotificationService;
import com.kh.osori.notification.model.vo.Notification;
import com.kh.osori.trans.model.vo.Grouptrans;
import com.kh.osori.user.controller.UserController;
import com.kh.osori.user.model.vo.User;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/group")
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j

public class GroupBudgetController {

    private final UserController userController;
    
	@Autowired
	private GroupBudgetService service;
	
	@Autowired
	private NotificationService notiService;

    GroupBudgetController(UserController userController) {
        this.userController = userController;
    }
	 
	@GetMapping("/gbList")
	public ResponseEntity<?> groupBudgetList(@RequestParam(value="userId") int userId) {
		List<GroupBudget> groupBudgetList = service.groupBudgetList(userId);

		if(groupBudgetList != null && !groupBudgetList.isEmpty()) {			
			return ResponseEntity.ok(groupBudgetList);
		}else if(groupBudgetList.size() == 0){
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("불러올 그룹가계부 목록이 없습니다.");
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("그룹가계부 목록조회를 실패했습니다.");
		}
	}
	
	@GetMapping("/gbOldList")
	public ResponseEntity<?> oldGroupBudgetList(@RequestParam(value="userId") int userId) {
		List<GroupBudget> oldGroupBudgetList = service.oldGroupBudgetList(userId);

		if(oldGroupBudgetList != null && !oldGroupBudgetList.isEmpty()) {			
			return ResponseEntity.ok(oldGroupBudgetList);
		}else if(oldGroupBudgetList.size() == 0){
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("불러올 그룹가계부 목록이 없습니다.");
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("이전 그룹가계부 목록조회를 실패했습니다.");
		}
	}
	
	
	@PostMapping("/gbAdd")
	public ResponseEntity<?> addNewGroupB(@RequestBody GroupBudget gb){
		int reuslt1 = service.addNewGroupB(gb);
		int result2 = 0;
		
		if(reuslt1 > 0) {
			int generatedId = gb.getGroupbId(); 
			
			//그룹 관리자 추가
			BudgetMem newMem = new BudgetMem();
			newMem.setGroupbId(generatedId);
			newMem.setRole("ADMIN");
			newMem.setUserId(gb.getUserId());
			result2 = service.addGroupAdmin(newMem);
			
		}
			
		if(result2 > 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body(gb);
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("그룹가계부 추가에 실패했습니다");
		}
	}
	
	@GetMapping("/searchMem")
	public ResponseEntity<?> searchGroupMemberList(@RequestParam("keyword") String keyword) {
		List<User> userList = service.searchGroupMemberList(keyword);
		
		if(userList != null && !userList.isEmpty()) {
			return ResponseEntity.ok(userList);
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 검색목록 불러오기 실패");
		}
	}
	
	@PostMapping("/gbAddMem")
	public ResponseEntity<?> addGroupMember(@RequestBody BudgetMem mem){
		int result = service.addGroupMember(mem);
		
		if(result > 0) {
			String adminLoginId = service.getAdminId(mem.getGroupbId());
			String getLoginId = service.getLoginid(mem.getUserId());
			String groupName = service.getGroupName(mem.getGroupbId());
			
			notiService.sendMessageToMembers(getLoginId, adminLoginId, adminLoginId+"님이 "+getLoginId+"님을 "+groupName+" 가계부에 초대하셨습니다.", "INVITE", mem.getGroupbId());
			
			return ResponseEntity.status(HttpStatus.CREATED).body("멤버 추가 성공");
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("멤버 추가에 실패했습니다");
		}
	}
	
	@PostMapping("/gbInviteNoti")
	public ResponseEntity<?> updateInviStatus(@RequestBody Map<String,Object> params){
		String status = (String)params.get("status");
		int inviteNum = Integer.parseInt(String.valueOf(params.get("inviteNum")));
        int receiver = Integer.parseInt(String.valueOf(params.get("receiver")));
		
		BudgetMem update = new BudgetMem();
		update.setStatus(status);
		update.setGroupbId(inviteNum);
		update.setUserId(receiver);
		
		int result = service.updateInviStatus(update);
		
		if(result > 0) {
			return ResponseEntity.ok(200);
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("초대 상태 변경에 실패했습니다.");		
		}
	}
	
	@PutMapping("/gbIsRead")
	public ResponseEntity<?> updateIsRead(@RequestParam(value="notiId") int notiId){
		int result = service.updateIsRead(notiId);
		
		if(result > 0) {
			return ResponseEntity.ok("읽음 처리 성공");
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("읽음 상태 변경에 실패했습니다.");		
		}
	}
	
	@GetMapping("/gbNotiList")
	public ResponseEntity<?> notiList(@RequestParam(value="loginId") String loginId){
		List<Notification> notiList = service.notiList(loginId);
		
		if(notiList != null && !notiList.isEmpty()) {
			return ResponseEntity.status(HttpStatus.CREATED).body(notiList);
		}else if(notiList.size() == 0){
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("불러올 알림이 없습니다");
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알림 목록 조회에 실패했습니다.");		
		}
	}
	
	@GetMapping("/gbCheckAdmin")
	public ResponseEntity<?> groupCheckAdmin(@RequestParam(value="groupbId") int groupbId) {
		int groupAdminId = service.groupCheckAdmin(groupbId);

		if(groupAdminId > 0) {			
			return ResponseEntity.ok(groupAdminId);
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("그룹 관리자 조회에 실패했습니다");
		}
	}
	
	@PostMapping("/gbUpdate")
	public ResponseEntity<?> updateGroupB(@RequestBody GroupBudget gb){
		int result = service.updateGroupB(gb);
		
		if(result > 0) {
			return ResponseEntity.ok(gb);
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("그룹 가계부 수정에 실패했습니다");
		}
	}
	
	@PostMapping("/gbDelete")
	public ResponseEntity<?> deleteGroupBudget(@RequestParam(value="groupbId") int groupbId){
		System.out.println(groupbId);
		int result = service.deleteGroupBudget(groupbId);
		
		if(result > 0) {
			String groupName = service.getGroupName(groupbId);
			notiService.deleteGroupAndNotify(groupbId, groupName);
			return ResponseEntity.ok("그룹 가계부 삭제 성공");
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("그룹 가계부 삭제에 실패했습니다");
		}
	}
	
	@GetMapping("/gbTrans")
	public ResponseEntity<?> groupTransactionList(@RequestParam("groupbId") int groupbId) {
	    List<Grouptrans> list = service.groupTransactionList(groupbId); 
	    
	    if(list != null) {
	        return ResponseEntity.ok(list);
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("거래 내역이 없습니다.");
	    }
	}
	
	@GetMapping("/gbAll")
	public ResponseEntity<?> groupBudgetAll(){
		List<Grouptrans> list = service.groupBudgetAll(); 
	    
	    if(list != null) {
	        return ResponseEntity.ok(list);
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("그룹 가계부가 없습니다.");
	    }
	}

}
