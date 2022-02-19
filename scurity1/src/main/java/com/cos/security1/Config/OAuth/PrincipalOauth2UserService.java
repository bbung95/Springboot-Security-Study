package com.cos.security1.Config.OAuth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.security1.Config.Auth.PrincipalDetails;
import com.cos.security1.Config.OAuth.Provider.FacebookUserInfo;
import com.cos.security1.Config.OAuth.Provider.GoogleUserInfo;
import com.cos.security1.Config.OAuth.Provider.KakaoUserInfo;
import com.cos.security1.Config.OAuth.Provider.NaverUserInfo;
import com.cos.security1.Config.OAuth.Provider.OAuth2UserInfo;
import com.cos.security1.Model.User;
import com.cos.security1.Repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	// 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
	// 함수 종료시 @AuthenticaitonPrincipal 어노테이션이 만들어진다
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		System.out.println("getClientRegistration : "+userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인했는지 확인
		System.out.println("getAccessToken : "+userRequest.getAccessToken().getTokenValue());
		
		OAuth2User oauth2User = super.loadUser(userRequest);
		// 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인 완료 -> code를 리턴(OAuth-client라이브러리) -> AccessToken 요
		// userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원프로필 받아준다.
		System.out.println("getAttributes : "+oauth2User.getAttributes());
		
		OAuth2UserInfo oauth2UserInfo = null;
		
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("페이스북 로그인");
			oauth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
		} else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
			System.out.println("페이스북 로그인");
			oauth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes());
		} else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			System.out.println("네이버 로그인");
			oauth2UserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("response"));
		} else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
			System.out.println("카카오 로그인");
			
			System.out.println("kakao : "+oauth2User.getAttributes());
			oauth2UserInfo = new KakaoUserInfo(oauth2User.getAttributes());
		} else {
			System.out.println("우리는 구글과 페이스북만 지원해요");
		}
		
		System.out.println("test : "+oauth2UserInfo);
		
		String provider = oauth2UserInfo.getProvider(); // Google
		String providerId = oauth2UserInfo.getProviderId();
		String username = provider+"_"+providerId; // Google_123123213231231231213213
		String password = new BCryptPasswordEncoder().encode("겟인데어");
		String email = oauth2UserInfo.getEmail();
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {
			// 회원가입 진행
			
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			
			userRepository.save(userEntity);
			
		}else {
			System.out.println("이미 회원가입이 완료 되어 있습니다.");
		}
		
		return new PrincipalDetails(userEntity, oauth2User.getAttributes());
	}
}
