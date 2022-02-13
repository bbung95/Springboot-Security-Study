package com.cos.security1.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity  // 필터 체인 관리 시작 어노테이션
@EnableGlobalMethodSecurity(securedEnabled = true , prePostEnabled = true) // secured 어노테이션 활성화 , preAuthorize / PostAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
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
			.loginPage("/login"); // 구글 로그인이 완료된 뒤의 처리가 필요함.
	}
	
	// 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다. 
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
}
