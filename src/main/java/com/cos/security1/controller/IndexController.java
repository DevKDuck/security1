package com.cos.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.config.auth.PrincipalDeatils;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Controller
public class IndexController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder; 
	
	//일반 로그인
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDeatils userDetails) {
		PrincipalDeatils principalDeatils = (PrincipalDeatils)authentication.getPrincipal(); //Object로 받아서 다운캐스팅
		System.out.println("일반 로그인 Authentication" + principalDeatils.getUser());
		
		System.out.println("일반 로그인 @AuthenticationPrincipal" + userDetails.getUser());
		return "testlogin";
	}
	
	//oauth2 로그인
		@GetMapping("/test/ouath/login")
		public @ResponseBody String testOauthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) {
			OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal(); //Object로 받아서 다운캐스팅
			System.out.println("ouath 로그인 Authentication" + oAuth2User.getAttributes());
			
			System.out.println("ouath 로그인 @AuthenticationPrincipal" + oauth.getAttributes());
			return "testlogin";
		}
	
	
	
	@GetMapping({"","/"})
	public String index() {
		return "index";
	}
	/*
	 * 페이지를 만들지 않아 임시 @ResponseBody붙임
	 */
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDeatils principalDeatils) {
		
		System.out.println("PrincipalDetail로 일반,Oauth2 로그인정보 가져오기"+principalDeatils.getUser());
		return "user";
	}
	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}
	
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@PostMapping("/join")
	public String join(User user) {
		System.out.println(user);
		user.setRole("ROLE_USER");
		String pwd = user.getPassword();
		String encodedPwd = bCryptPasswordEncoder.encode(pwd);
		user.setPassword(encodedPwd);
		userRepository.save(user); //패스워드 암호화 필요
		return "redirect:/loginForm";
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "info";
	}
	
	/*
	 * PreAuthorize - 메서드 실행전 확인
	 * PostAuthorize - 메서드 실행후 확인
	 */
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "data";
	}
}

/*
  mvc:
    view:
      prefix: /templates/
      suffix: .mustache

*/
