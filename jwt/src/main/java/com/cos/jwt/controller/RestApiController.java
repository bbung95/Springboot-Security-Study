package com.cos.jwt.controller;

import com.cos.jwt.domain.User;
import com.cos.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class RestApiController {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @GetMapping("home")
    public String home(){
        return "<h1>home</h1>";
    }

    @PostMapping("token")
    public String token(){
        return "<h1>token</h1>";
    }

    @PostMapping("join")
    public String join(@RequestBody User user){

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        userRepository.save(user);

        return "회원가입완료";
    }

    // user, manager, admin 권한만 접근 가능
    @GetMapping("user")
    public String user(){
        return "user";
    }

    // manager, admin 권한만 접근 가능
    @GetMapping("manager")
    public String manager(){
        return "manager";
    }

    // admin 권한만 접근 가능
    @GetMapping("admin")
    public String admin(){
        return "admin";
    }
}
