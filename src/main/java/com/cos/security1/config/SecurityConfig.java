
package com.cos.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.cos.security1.config.auth.oatuh.PrincipalOAuth2UserService;

@Configuration
@EnableWebSecurity // 스프링시큐리티 필터가 스프링 필터체인에 등록
@EnableMethodSecurity(securedEnabled=true, prePostEnabled = true) // 필터 체인외에 개별api에 어노테이션으로 등록해 권한 처리 가능
public class SecurityConfig {

	@Autowired
	private PrincipalOAuth2UserService principalOAuth2UserService;
	
	@Bean
	BCryptPasswordEncoder encodedPwd() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(
						authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
						.requestMatchers("/user/**").authenticated() //인증만 되면 들어가도록 함
						.requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN") //자동으로 ROLE_접두어 생성해줌
						.requestMatchers("/admin/**").hasRole("ADMIN") //hasAnyRole은 하나라도 가지고 있으면 hasRole은 한가지
						.anyRequest().permitAll()
						)
				.formLogin(
						formLoginCustomizer -> formLoginCustomizer
						.loginPage("/loginForm")
						.loginProcessingUrl("/login") // login 호출시 시큐리티가 낚아채서 대신 로그인
						.defaultSuccessUrl("/") //특정 페이지 -> 성공시 특정페이지로 이동
						)
				.oauth2Login(oauth2Customizer -> oauth2Customizer
						.loginPage("/loginForm")
						.userInfoEndpoint(
								userInfo -> userInfo.userService(principalOAuth2UserService)
								)
						)
				/*
				 * 구글 로그인이 완료된 후 후처리
				 * 코드를 받아와서 액세스토큰으로 사용자의 정보를 가져오는게 보통
				oauth2 라이브러리 이용시 코드 대신 
				액세스 토과 사용자 정보를 가져오도록 할 수 있음
				 */
				.csrf(AbstractHttpConfigurer::disable)
				.build();
	}

}
