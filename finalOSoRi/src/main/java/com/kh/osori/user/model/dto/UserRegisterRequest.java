package com.kh.osori.user.model.dto;

import com.kh.osori.user.model.vo.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserRegisterRequest {
	
	private User user;
	private String loginType;     
    private String providerUserId;

}
