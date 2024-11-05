package com.cos.security1.config.auth.oatuh.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

	private Map<String, Object> attributes;
	
	public KakaoUserInfo(Map<String,Object> attributes) {
		this.attributes = attributes;
	}
	
	@Override
	public String getProviderId() {
		String str = "";
		return str + attributes.get("id");
	}

	@Override
	public String getPorvider() {
		return "kakao";
	}

	@Override
	public String getEmail() {
		//email은 가져오지 않음
		return "no email data received OAuth 2.0";
	}

	@Override
	public String getName() {
		/*
		  구조가 { 
		  id = 값,
		  properties = {
		  	nickname
		  } 
		 */
				
		Map<String,Object> properties = (Map<String,Object>) attributes.get("properties");
		return (String)properties.get("nickname");
	}

}
