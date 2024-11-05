package com.cos.security1.config.auth.oatuh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.security1.config.auth.PrincipalDeatils;
import com.cos.security1.config.auth.oatuh.provider.FackbookUserInfo;
import com.cos.security1.config.auth.oatuh.provider.GithubUserInfo;
import com.cos.security1.config.auth.oatuh.provider.GoogleUserInfo;
import com.cos.security1.config.auth.oatuh.provider.OAuth2UserInfo;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	//구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("getClientRegistration: " + userRequest.getClientRegistration()); //registrationId로 어떤 oauth로 로그인 했는지 확인
		System.out.println("getAccessToken: " + userRequest.getAccessToken().getTokenValue());
		
		OAuth2User oAuth2User = super.loadUser(userRequest);
		//구글 로그인 버튼 클릭 => 로그인 성공 => 코드 => code 리턴(OAuth-client 라이브러리) => 액세스 토큰 요청
		//userRequest 정보 => LoadUser 함수 호출 => 구글로 부터 회원 정보 받음
		System.out.println("getAttributes: " + oAuth2User.getAttributes());
		
		
		//OAuthClient 라이브러리에서는 Google,Facebook,GitHub,Okta 만 provider를 제공한다고 함
		OAuth2UserInfo oAuth2UserInfo = null;
		
		if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
			System.out.println("google login");
		}
		else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
			oAuth2UserInfo = new FackbookUserInfo(oAuth2User.getAttributes());
			System.out.println("facebook login");
		}
		else if (userRequest.getClientRegistration().getRegistrationId().equals("github")) {
			oAuth2UserInfo = new GithubUserInfo(oAuth2User.getAttributes());
			System.out.println("github login");
		}
		else {
			System.out.println("다른 설정 안한 Oauth2 login");
			
		}
		
		//회원가입 강제로 진행
		String provider = oAuth2UserInfo.getPorvider();
		String providerId = oAuth2UserInfo.getProviderId();
		String username = provider + "_" + providerId; // ex) google_id
		String password = bCryptPasswordEncoder.encode("Devkduck"); //oauth 로그인시 비밀번호 쓰지 않아 필요없는값
		String email = oAuth2UserInfo.getProviderId();
		String role = "ROLE_USER";
		
		User user = userRepository.findByUsername(username);
		
		if(user == null) {
			user = User.builder()
					.username(username)
					.provider(provider)
					.providerId(providerId)
					.password(password)
					.email(email)
					.role(role)
					.build();
			
			userRepository.save(user);
			System.out.println("oauth로 회원가입 진행하였습니.");
		}
		else {
			System.out.println("기존 oauth로 회원가입 하신 아이디입니다.");
		}
		return new PrincipalDeatils(user, oAuth2User.getAttributes());
	}
}

