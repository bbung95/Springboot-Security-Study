package com.cos.security1.Config.Auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cos.security1.Model.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 시큐리티가 /login 주소 요청 오면 낚아채서 로그인을 진행시킨다.
// 로그인을 진행이 완료가 되면 시큐리티 session으 만들어 줍니다 (Security ContextHolder)
// 오프젝트 타입 => Authentication 타입 객체
// Authentication 안에 User 정보가 있어야 됨.
// User 오브젝트타입 => UserDetails 타입 객체

// Security Session => Authentication => UserDetails(PrincipalDetails)

@ToString
@Setter
@Getter
public class PrincipalDetails implements UserDetails , OAuth2User{
	
	private User user;
	private Map<String , Object> attributes;
	
	// 일반 로그인
	public PrincipalDetails(User user) {
		this.user = user;
	};
	
	//OAuth 로그인
	public PrincipalDetails(User user , Map<String , Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	};
	
	// 해당 User의 권한을 리턴하는 곳!
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<GrantedAuthority>();
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
	
	// 계정 만료
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 계정 잠김
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	// 비밀번호가 오래 되었니
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	// 계정 활성화
	@Override
	public boolean isEnabled() {
		
		// 사이트에서 유저가 1년동안 로그인을 안했다면!! 휴먼 계정으로 하기로 함
		// 현재시간 - 로긴시간 => 1년을 초과하면 false
		
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return null;
	}

}
