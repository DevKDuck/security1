package com.cos.security1.config.auth.oatuh.provider;

import java.util.Map;

public class FackbookUserInfo implements OAuth2UserInfo {

	private Map<String, Object> attributes;
	
	public FackbookUserInfo(Map<String,Object> attributes) {
		this.attributes = attributes;
	}
	
	@Override
	public String getProviderId() {
		return (String) attributes.get("id");
	}

	@Override
	public String getPorvider() {
		return "fackebook";
	}

	@Override
	public String getEmail() {
		return (String)attributes.get("email");
	}

	@Override
	public String getName() {
		return (String)attributes.get("name");
	}

}
