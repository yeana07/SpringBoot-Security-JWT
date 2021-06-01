package com.cos.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFilter3 implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		// 코스라는 토큰을 만들었다고 가정
		// 코스 토큰이 넘어오면 필터를 거쳐 인증O,
		// 아니면 필터에서 나가고 컨트롤러에 진입 못 하게 하기
		if (req.getMethod().equals("POST")) {
			System.out.println("POST 요청됨");
			String headerAuth = req.getHeader("Authorization");
			System.out.println(headerAuth);
			System.out.println("필터3");

			if (headerAuth.equals("cos")) {
				chain.doFilter(req, res);
			} else {
				PrintWriter out = res.getWriter();
				out.println("인증안됨");
			}
		}

	}

}
