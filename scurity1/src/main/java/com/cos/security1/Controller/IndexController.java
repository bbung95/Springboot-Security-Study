package com.cos.security1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.Config.Auth.PrincipalDetails;
import com.cos.security1.Model.User;
import com.cos.security1.Repository.UserRepository;

@Controller
public class IndexController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("test/login")
	@ResponseBody
	public String loginTest(Authentication authentication, @AuthenticationPrincipal UserDetails userDetail) { // DI(의존성 주입)
		
		System.out.println((UserDetails)authentication.getPrincipal()); // Authentication
		System.out.println(userDetail); // @AuthenticationPrincipal
		
		return "세션 정보 확인하기";
	}
	
	@GetMapping("test/oauth/login")
	@ResponseBody
	public String oauthLoginTest(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) { // DI(의존성 주입)
		
		System.out.println((OAuth2User)authentication.getPrincipal()); // Authentication
		System.out.println(oauth); // @AuthenticationPrincipal
		
		return "OAuth 세션 정보 확인하기";
	}
	
	@GetMapping("/")
	public String index(Model model) {
		
		model.addAttribute("success", "index");
		
		return "index";
	}
	
	@GetMapping("user")
	public String user(Model model , @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		System.out.println("principalDetails : "+principalDetails.getUser());
		model.addAttribute("success", "user");
		
		return "index";
	}
	
	@GetMapping("admin")
	public String admin(Model model) {
		
		model.addAttribute("success", "admin");
		
		return "index";
	}
	
	@GetMapping("manager")
	public String manager(Model model) {
		
		model.addAttribute("success", "manager");
		
		return "index";
	}
	
	// 로그인 뷰
	@GetMapping("login")
	public String login() {
		
		return "login";
	}
	
	// 회원 가입 뷰
	@GetMapping("join")
	public String join() {
		
		return "join";
	}
	
	// 회원가입
	@PostMapping("join")
	public String join(User user) {
		
		user.setRole("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = passwordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		
		userRepository.save(user); // 비밀번호 암호화가 필여하다. 시큐리티 로그인 불가능
		
		return "redirect:/login";
	}
	
	@Secured("ROLE_ADMIN") // 특정 메서드만 권한 주기
	@GetMapping("info")
	@ResponseBody
	public String info() {
		
		return "개인정보";
	}	
	
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // data 접근 전
	@GetMapping("data")
	@ResponseBody
	public String data() {
		
		return "데이터정보";
	}

}