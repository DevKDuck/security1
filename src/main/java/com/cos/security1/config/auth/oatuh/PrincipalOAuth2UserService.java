package com.cos.security1.config.auth.oatuh;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService{
	
	//구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("getClientRegistration: " + userRequest.getClientRegistration()); //registrationId로 어떤 oauth로 로그인 했는지 확인
		System.out.println("getAccessToken: " + userRequest.getAccessToken().getTokenValue());
		//구글 로그인 버튼 클릭 => 로그인 성공 => 코드 => code 리턴(OAuth-client 라이브러리) => 액세스 토큰 요청
		//userRequest 정보 => LoadUser 함수 호출 => 구글로 부터 회원 정보 받음
		
		System.out.println("getAttributes: " + super.loadUser(userRequest).getAttributes());
		
		OAuth2User oAuth2User = super.loadUser(userRequest);
		return super.loadUser(userRequest);
	}
}

