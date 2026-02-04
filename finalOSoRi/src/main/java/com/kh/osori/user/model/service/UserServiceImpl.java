package com.kh.osori.user.model.service;

import java.util.HashMap;

import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.kh.osori.badge.dao.BadgeDao;
import com.kh.osori.user.model.dao.UserDao;
import com.kh.osori.user.model.dto.UserRegisterRequest;
import com.kh.osori.user.model.vo.User;
import com.kh.osori.util.JwtUtil;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao dao;
	
	@Autowired
    private BadgeDao badgeDao; // 뱃지용 전용 DAO 주입
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Autowired
	private JwtUtil jwtUtil; // 토큰 발급용 
	
	@Transactional
	@Override // 회원 가입 
	public int insertUser(UserRegisterRequest request) {
		
		User user = request.getUser(); 
		
		String loginType = request.getLoginType();
		
		String providerUserId = request.getProviderUserId();
		
		int result1 = dao.insertUser(sqlSession, user);
		
		int currentUserId = dao.selectUserId(sqlSession);
		
		HashMap<String, Object> accountMap = new HashMap<>();
		
		accountMap.put("loginType", loginType);
		accountMap.put("providerUserId", providerUserId);
		accountMap.put("currentUserId", currentUserId);
		
		int result2 = dao.insertAuthAccount(sqlSession, accountMap); 
		
		int sum = result1 + result2; 
		
		if(sum >= 2) {
            int userId = user.getUserId(); 
            int defaultBadgeNo = 1; 
            
            badgeDao.insertDefaultBadge(userId, defaultBadgeNo);
        }
		
		return sum; 
		
	}
	
	@Transactional
	@Override // 로그인 메서드, 동시에 마지막 로그인 날짜 SYSDATE를 넣어보기  (트랜잭션 처리를 이용해보자.) 
	public User selectUser(User user) {
		
		int result = 0; // 트랜잭션 처리를 한 후, 결과값 받는 변수 
		
		User loginUser = dao.selectUser(sqlSession, user); // 사용자가 입력한 아이디 및 비밀번호를 데이터 베이스에서 있는지 조회.
		
		//위에 로그인 멤버는 데이터베이스에서 조회만 해온거지 휴면 처리 이런건 전혀 안되어있다. 
		
		if(loginUser!=null) { // DB에서 회원 조회가 됐다면 
			
			if(loginUser.getStatus().equals("N")) { // 탈퇴한 회원은 아래 updateDate를 거칠 필요가 없다. 
				return loginUser;
			}
			
			// 조회가 끝나면 -> 휴면 계정인지 아닌지를 체크 하고 마지막 로그인 날짜까지 갱신  
			
			result = dao.updateDate(sqlSession,loginUser); // 로그인 한 시점을 마지막 로그인 날짜로 바꾸기
			
			//이때 DB는 수정 됐지만 로그인 멤버 객체는 수정이 안됐다. (휴면 판단 여부 및 마지막 로그인 날짜 처리) 
			
			loginUser = dao.selectUser(sqlSession, user); // 갱신 하고나서 로그인 멤버 한번 더 초기화. 
			
			//업데이트 할때 SYSDATE - LAST_LOGIN >= 30 조건 걸어두기
			
			if(result > 0) { // 아이디및 비밀번호랑 일치한 회원 정보가 있고 마지막 로그인 날짜까지 수정 됐다면 로그인 승인. 
				return loginUser; 
			} 
		}
		
		return null; // 이 경우는 loginMember가 null이든 아니든 마지막 로그인 날짜 시점을 처리하지 못하면 실패로 처리. 
		 
	}
	

	@Override
	public Map<String, Object> processKakaoLogin(String code) {
	    
		// 카카오 토큰 받기
	    RestTemplate rt = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

	    
	    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	    params.add("grant_type", "authorization_code");
	    params.add("client_id", "fbeeefb1ab0d16e849dfdfdd01f9222b"); //
	    params.add("redirect_uri", "http://localhost:5173/auth/kakao/callback");
	    params.add("code", code);

	    // 카카오 보안 설정에서 Client Secret을 생성했다면 반드시 아래 코드를 추가해야 401 에러가 안 납니다.
	   
	    HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);
	    ResponseEntity<Map> tokenResponse = rt.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, tokenRequest, Map.class);
	    String accessToken = (String) tokenResponse.getBody().get("access_token");

	    // 2. 사용자 정보 가져오기 (닉네임 + 이메일)
	    headers = new HttpHeaders();
	    headers.add("Authorization", "Bearer " + accessToken);
	    HttpEntity<MultiValueMap<String, String>> profileRequest = new HttpEntity<>(headers);
	    ResponseEntity<Map> profileResponse = rt.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, profileRequest, Map.class);
	    
	    Map<String, Object> body = profileResponse.getBody();
	    Map<String, Object> kakaoAccount = (Map<String, Object>) body.get("kakao_account");
	    Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
	    
	    String email = (String) kakaoAccount.get("email"); //에서 권한을 얻어야 null이 안 나옵니다.
	    String nickName = (String) profile.get("nickname"); //
	    
	    String providerUserId = String.valueOf(body.get("id")); // 고유 토큰 아이디
	    String loginType = "KAKAO"; // 로그인 타입

	    // 3. DB 가입 확인 및 처리 (이메일 기준)
	    User user = dao.findLoginIdByEmail(sqlSession, email); // 회원 조회
	    
	    Map<String, Object> result = new HashMap<>();
	    
	    if (user == null) {

	    		result.put("isNewMember", true); // 추가
	    		result.put("email",email);
	    		result.put("nickName", nickName);
	    		result.put("providerUserId", providerUserId); // 토큰 고유 아이디
	    		result.put("loginType","KAKAO");
	    		
	    		return result;
	    	
	    	
	    }
	    
	    int rowUpdate = dao.updateDate(sqlSession,user); // 업데이트 된 행이 있는지 판별
	    user = dao.findLoginIdByEmail(sqlSession, email); // 업데이트 된 유저 객체 한번 더 호출
	    

	    if(rowUpdate > 0) { // lastLogin 날짜 갱신 됐는가 ? 

	    	
	    		// 4. 전용 JWT 발행
		    String token = jwtUtil.generateToken(user.getLoginId());
		    user.setPassword(null);
		    result.put("token", token);
		    result.put("user", user);
		    
	        if("H".equals(((User)result.get("user")).getStatus())) {
        			result.put("message", "휴면 회원 입니다. 휴면 해제를 해주세요.");
	        } else if ("N".equals(((User)result.get("user")).getStatus())) {
	        		result.put("message", "탈퇴한 회원입니다."); 
	        }
		   
		    return result; // 날짜가 갱신이 되면 로그인 성공 처리 

	    	
	    }
	    return null; // 날짜가 갱신이 안되면 바로 로그인 실패 처리 
	    
	    
	}
	
	
	@Override // 아이디 중복체크 
	public int idCheck(String loginId) {
		int result = dao.idCheck(sqlSession, loginId);
		
		return result; 
	}
	
	@Override // 닉네임 중복체크
	public int nickNameCheck(String nickName) {
		int result = dao.nickNameCheck(sqlSession, nickName);
		
		return result; 
	}
	
	@Override // 이메일 중복체크
	public int emailCheck(String email) {
		
		int result = dao.emailCheck(sqlSession, email);
		
		return result; 
		
	}
	
	@Override // 회원 정보 업데이트 
	public int updateUser(User loginUser) {
		int result = dao.updateUser(sqlSession,loginUser);
		
		return result; 
	}
	
	//회원 상태 N으로 바꾸기 (로그인 불가능 하게) 
	@Override
	public int deleteUser(User loginUser) {
		int result = dao.deleteUser(sqlSession,loginUser); 
		
		return result; 
	}
	
	//비밀번호 변경 메소드 
	@Override
	public int changeUserPwd(User loginUser) {
		int result = dao.changeUserPwd(sqlSession, loginUser); 
			
		return result; 
	}
	
	//loginId를 기반으로 사용자 정보 갖고오는 메소드 
	@Override
	public User selectByLoginId(String loginId) {
		User loginUser = dao.selectByLoginId(sqlSession, loginId);
		
		return loginUser; 
	}
	
	//이메일을 기반으로 loginId 찾는 메소드 
	@Override
	public User findLoginIdByEmail(String email) {
		
		User loginUser = dao.findLoginIdByEmail(sqlSession, email);
		
		return loginUser; 
	}
	
	//비밀번호 재설정 하는 메소드
	@Override
	public int resetPassword(Map<String, String> userMap) {
		
		int result = dao.resetPassword(sqlSession, userMap);
		
		return result; 
		
	}

}