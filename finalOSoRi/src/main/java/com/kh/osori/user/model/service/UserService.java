package com.kh.osori.user.model.service;

import java.util.Map;

import com.kh.osori.user.model.dto.UserRegisterRequest;
import com.kh.osori.user.model.vo.User;

public interface UserService {
	
	int insertUser(UserRegisterRequest request); // 회원 가입 메소드 
	int idCheck(String inputId); // 아이디 중복 체크 메소드 
	int nickNameCheck(String nickName); // 닉네임 중복 체크 메소드
	int emailCheck(String email); // 이메일 중복 체크 메소드
	User selectUser(User user); // 회원 조회, 마지막 로그인 날짜 갱신 및 휴면 계정 처리 메소드 
	int updateUser(User loginUser); // 정보 수정 
	int deleteUser(User loginUser); // 회원 탈퇴 메소드
	int changeUserPwd(User loginUser); // 비밀번호 변경 메소드
	User selectByLoginId(String loginId); // 아이디로 회원 정보 조회하는 메소드 
	User findLoginIdByEmail(String email);
	int resetPassword(Map<String, String> userMap); 
	
	// 카카오 로그인 처리 메소드 
	Map<String, Object> processKakaoLogin(String code);
	
}