package com.cos.security1.Config.OAuth.Provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{

	private Map<String , Object> attributes; // OAuth2User.getAttriutes();
	
	public KakaoUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String getProviderId() {
		
		return Integer.toString((int)attributes.get("id"));
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getEmail() {
		
		Map<String , Object> account = (Map)attributes.get("kakao_account");
		
		return (String)account.get("email");
	}

	@Override
	public String getName() {
		return (String)attributes.get("properties");
	}

}
