package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링시큐리티 필터가 스프링 필터체인에 등록
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(
						authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
						.requestMatchers("/user/**").authenticated()
						.requestMatchers("/manager/**").hasAnyRole("manager", "admin")
						.requestMatchers("/admin/**").hasAnyRole("admin")
						.anyRequest().permitAll()
						)
				.formLogin(
						formLoginCustomizer -> formLoginCustomizer
						.loginPage("/login")
						.defaultSuccessUrl("/")
						)
				.csrf(AbstractHttpConfigurer::disable)
				.build();
	}

}
