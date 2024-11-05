package com.cos.security1.config.auth.oatuh.provider;

//OAuth2 별로 데이터가 상이하기 떄문에 인터페이스 생성
public interface OAuth2UserInfo {
	String getProviderId();
	String getPorvider();
	String getEmail();
	String getName();
}
