package com.kh.osori.groupBudget.model.service;

import java.util.List;

import com.kh.osori.groupBudget.model.vo.BudgetMem;
import com.kh.osori.groupBudget.model.vo.GroupBudget;
import com.kh.osori.user.model.vo.User;

public interface GroupBudgetService {
	public List<GroupBudget> groupBudgetList(int userId); //그룹가계부 리스트
	public int addNewGroupB(GroupBudget gb); //그룹가계부 생성
	public int addGroupAdmin(BudgetMem newMem); //그룹가계부 관리자생성
	public void editBAmount(); //그룹가계부 예산 수정
	public List<User> searchGroupMemberList(String keyword); //그룹가계부 회원리스트
	public void deleteUser(); //그룹가계부 회원 삭제
	public void deleteGroupBudget(); //그룹가계부 삭제
	public void updateGroupBudget(); //그룹가계부 수정
	public int addGroupMember(BudgetMem mem); //회원 추가
}
