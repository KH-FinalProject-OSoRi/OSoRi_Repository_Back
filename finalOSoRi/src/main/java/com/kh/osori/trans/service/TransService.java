package com.kh.osori.trans.service;
import java.util.List;
import com.kh.osori.trans.model.vo.Grouptrans;
import com.kh.osori.trans.model.vo.Mytrans;

import com.kh.osori.trans.model.vo.Grouptrans;

public interface TransService {

	public int myTransSave(Mytrans mt);
	public int GroupTransSave(Grouptrans gt);
	public List<Mytrans> getMyTransactions(int userId);
	public int updateTrans(Mytrans mt);
	public int deleteTrans(int transId);
	public List<Grouptrans> getGroupTransactions(int groupId);
	public int updateGroupTrans(Grouptrans gt);
	public int deleteGroupTrans(int transId);
	public int mergeFixedToMyTrans();
	public Object groupInfo(int groupId);
	public List<String> getGroupMemberIds(int groupBId);
	public String getLoginId(int userId);
	public String getGroupTitle(int groupBId);
	public List<Mytrans> recentTrans(int userId);

}
