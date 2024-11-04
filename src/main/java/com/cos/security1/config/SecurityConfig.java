
package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링시큐리티 필터가 스프링 필터체인에 등록
public class SecurityConfig {

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
						.requestMatchers("/manager/**").hasAnyRole("manager", "admin")
						.requestMatchers("/admin/**").hasAnyRole("admin")
						.anyRequest().permitAll()
						)
				.formLogin(
						formLoginCustomizer -> formLoginCustomizer
						.loginPage("/loginForm")
						.loginProcessingUrl("/login") // login 호출시 시큐리티가 낚아채서 대신 로그인
						.defaultSuccessUrl("/") //특정 페이지 -> 성공시 특정페이지로 이동
						)
				.csrf(AbstractHttpConfigurer::disable)
				.build();
	}

}
