package com.cos.jwt.config.jwt;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

// 스프링 시큐리티에 UsernamePasswordAuthenticationFilter가 있음
// /login 요청해서 username, password를 post로 전송하면 해당 필터가 동작
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final AuthenticationManager authenticationManager;

	// /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("JwtAuthenticationFilter: 로그인 시도 중");
		
		// 1. username, password 받기
		try {
			/*
			BufferedReader br = request.getReader();
			
			String input = null;
			while((input = br.readLine()) != null) {
				System.out.println(input);
			}
			*/
			
			ObjectMapper om = new ObjectMapper();
			User user = om.readValue(request.getInputStream(), User.class);
			System.out.println(user);
			
			
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
			
			// PrincipalDetailsService의 loadByUsername()함수가 실행됨
			// 함수 실행 수 정상이면 authentication이 리턴됨 - 인증완료
			// db에 있는 username과 password가 일치한다는 뜻
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
		
			
			// 출력이 되면 로그인이 되었다는 뜻
			PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
			System.out.println("로그인 완료: "+principalDetails.getUser().getUsername());
			
			// 리턴으로 authentication 객체(로그인한 정보)를 session영역에 저장
			// JWT를 사용 하지만 권한처리 위해 session에 담기 -> security가 권한 관리
			return authentication;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	// attemptAuthentication()실행 후 인증이 정상적으로 완료시 successfulAuthentication()실행
	// JWT 토큰을 만들어 request한 사용자에게 JWT토큰을 response
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		System.out.println("인증 완료 후 successfulAuthentication() 실행");	
		
		PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();
		
		// HMAC 방식으로 sign
		// HMAC 방식: 서버만 아는 시크릿키값을 가짐(더 많이 사용)
		// RSA 방식: 공캐키와 개인키 사용
		String jwtToken = JWT.create()
				.withSubject("yena token")
				.withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME)) // 60초 * 10
				.withClaim("id", principalDetails.getUser().getId())
				.withClaim("username", principalDetails.getUser().getUsername())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));
			
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);
	}
}
