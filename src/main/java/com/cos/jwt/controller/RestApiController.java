package com.cos.jwt.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class RestApiController {
	
	private final UserRepository userRepository;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/home")
	public String home() {
		return "<h1>home</h1>";
	}
	
	@PostMapping("/token")
	public String token() {
		return "<h1>token</h1>";
	}
	
	
	@GetMapping("/joinForm")
	public String joinForm() {
		System.out.println("joinForm");
		return "joinForm";
	}
	
	
	@PostMapping("/join")
	public String join(@RequestBody User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles("ROLE_USER");
		userRepository.save(user);
		return "회원가입 완료";
	}
	
	// user, manager, admin 권한만 접근 가능
	@GetMapping("/api/v1/user")
	public String user(Authentication authentication) {
		// 세션이 제대로 만들어졌는지 확인
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("authentication: "+principal.getUsername());
		return "user";
	}

	// manager, admin 권한만 접근 가능
	@GetMapping("/api/v1/manager")
	public String manager() {
		
		return "manager";
	}
	
	// admin 권한만 접근 가능
	@GetMapping("/api/v1/admin")
	public String admin() {
		
		return "admin";
	}
}
