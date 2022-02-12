package com.cos.security1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.security1.Model.User;
import com.cos.security1.Repository.UserRepository;

@Controller
public class IndexController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/")
	public String index(Model model) {
		
		model.addAttribute("success", "index");
		
		return "index";
	}
	
	@GetMapping("user")
	public String user(Model model) {
		
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

}