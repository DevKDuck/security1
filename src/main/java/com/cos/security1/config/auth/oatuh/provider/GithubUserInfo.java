package com.cos.security1.config.auth.oatuh.provider;

import java.util.Map;

public class GithubUserInfo implements OAuth2UserInfo {

	private Map<String, Object> attributes;
	
	public GithubUserInfo(Map<String,Object> attributes) {
		this.attributes = attributes;
	}
	
	@Override
	public String getProviderId() {
		//id 가 int 자료형이라 형변환 따로 필요
		String str = "";
		return str + attributes.get("id");
	}

	@Override
	public String getPorvider() {
		return "github";
	}

	@Override
	public String getEmail() {
		if (attributes.get("email") == null) {
			//email 을 private 으로 설정되어 있으면 login 아이디로 설정
			return (String)attributes.get("login");
		}else {
			return (String)attributes.get("email");
		}
		
	}

	@Override
	public String getName() {
		return (String)attributes.get("name");
	}

}
