package com.cos.security1.config.auth.oatuh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.security1.config.auth.PrincipalDeatils;
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
		
		//회원가입 강제로 진행
		String provider = userRequest.getClientRegistration().getRegistrationId(); //google
		String providerId = oAuth2User.getAttribute("sub"); //pkID
		String username = provider + "_" + providerId; // ex) google_id
		String password = bCryptPasswordEncoder.encode("Devkduck"); //oauth 로그인시 비밀번호 쓰지 않아 필요없는값
		String email = oAuth2User.getAttribute("email");
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

