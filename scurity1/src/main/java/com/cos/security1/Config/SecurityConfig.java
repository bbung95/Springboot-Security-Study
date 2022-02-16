package com.cos.security1.Config;

import org.springframework.beans.factory.annotation.Autowired;

//구글 로그인이 완료된 뒤의 처리가 필요함. 
// 1. 코드받기(인증)
// 2. 엑세스토큰(권한)
// 3. 사용자 프로필 정보를 가져오고
// 4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
// 4-2. (이메일, 전화번호 , 이름 , 아이디) 쇼핑몰 -> (집주소) , 백화점몰 -> (등급)
		
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.security1.Config.Auth.PricipalDetailsService;
import com.cos.security1.Config.OAuth.PrincipalOauth2UserService;

@Configuration
@EnableWebSecurity  // 필터 체인 관리 시작 어노테이션
@EnableGlobalMethodSecurity(securedEnabled = true , prePostEnabled = true) // secured 어노테이션 활성화 , preAuthorize / PostAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated() // 인증만 되면 들어갈 수 있는 주소!!
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
		.and()
			.formLogin() 					// 기본 인증 로그인 페이지
			.loginPage("/login")
			.usernameParameter("username") // 임의의 유저 파라메터값
			.passwordParameter("password") // 임의의 유저 패스워드값
			.loginProcessingUrl("/login") // 시큐리티가 주소를 낚아채 시큐리티 로그인을 진행해줍니다.
			.defaultSuccessUrl("/")
		.and()
			.oauth2Login()
			.loginPage("/login")
			.userInfoEndpoint()
			.userService(principalOauth2UserService); 
			// 구글 로그인이 완료된 뒤의 후처리가 필요함. Tip.코드 X , (엑세스토큰+사용자프로필정보 O )
	}
	
	// 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다. 
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
}
