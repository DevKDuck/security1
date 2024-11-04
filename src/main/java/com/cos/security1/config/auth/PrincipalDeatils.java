package com.cos.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cos.security1.model.User;

import lombok.Data;

/*
시큐리티가 로그인 낚아채서 로그인 진행시킴
로그인 진행이 완료시 시큐리티가 가지고 있는 세션을 만들어줌 (Security ContextHolder)
세션안에 들어가는 오브젝트 는 Authentication타입의 객체여야한다.
Authentication안에 User정보가 있어야 됨
UserObject의 타입은 UserDetails타입 객체임

Security Session => Authentication => UserDeatails
 */

@Data
public class PrincipalDeatils implements UserDetails,OAuth2User{
	
	private User user;
	private Map<String,Object> attributes;
	
	public PrincipalDeatils(User user) {
		this.user = user;
	}
	
	public PrincipalDeatils(User user, Map<String,Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}

	//해당 유저의 권한을 리턴하는 곳
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	}


	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
