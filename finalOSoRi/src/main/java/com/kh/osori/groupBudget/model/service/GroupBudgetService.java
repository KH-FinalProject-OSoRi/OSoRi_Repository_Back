package com.kh.osori.groupBudget.model.service;

import java.util.List;

import com.kh.osori.challenges.model.vo.GroupChall;
import com.kh.osori.groupBudget.model.vo.BudgetMem;
import com.kh.osori.groupBudget.model.vo.GroupBudget;
import com.kh.osori.notification.model.vo.Notification;
import com.kh.osori.trans.model.vo.Grouptrans;
import com.kh.osori.user.model.vo.User;

public interface GroupBudgetService {
	public List<GroupBudget> groupBudgetList(int userId); //그룹가계부 리스트
	public List<GroupBudget> oldGroupBudgetList(int userId); //이전그룹가계부 리스트
	public int addNewGroupB(GroupBudget gb); //그룹가계부 생성
	public int addGroupAdmin(BudgetMem newMem); //그룹가계부 관리자생성
	public List<User> searchGroupMemberList(String keyword); //그룹가계부 회원리스트
	public void deleteUser(); //그룹가계부 회원 삭제
	public int deleteGroupBudget(int groupbId); //그룹가계부 삭제
	public int addGroupMember(BudgetMem mem); //회원 추가
	public int updateGroupB(GroupBudget gb); //예산금액 수정
	public String getAdminId(int groupbId); //관리자 아이디
	public String getLoginid(int userId); //회훤아이디 로그인아이디로
	public int updateInviStatus(BudgetMem update); //초대 요청 상태 변경
	public int updateIsRead(int notiId); //알림 읽음 처리
	public List<Notification> notiList(String loginId); //안읽은 알림 목록 조회
	public List<Grouptrans> groupTransactionList(int groupbId);
	public int groupCheckAdmin(int groupbId); //관리자 아이디 조회
}
