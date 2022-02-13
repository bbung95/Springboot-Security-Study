package com.cos.security1.Config.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.security1.Model.User;
import com.cos.security1.Repository.UserRepository;

// 시큐리티 설정에서 loginProcessingUrl("/login")
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 함수 실행

@Service
public class PricipalDetailsService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	// 시큐리티 session = Authentication = UserDetails
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		System.out.println("UserName : "+ username);
		
		User user = userRepository.findByUsername(username);
		
		System.out.println("user : "+ user);
		
		if(user != null) {
			return new PrincipalDetails(user);
		}
		
		return null;
	}

}
