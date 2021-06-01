package com.cos.jwt.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Data;

@Data
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String username;
	private String password;
	private String roles; // USER, ADMIN
	
	public List<String> getRoleList(){ // 하나의 유저 당 2개 이상의 롤이 있을 때
		if(this.roles.length() > 0) {
			return Arrays.asList(this.roles.split(","));
		}
		return new ArrayList<>();
	}
	
	/*
	@Builder
	public User(String username, String password, String roles) {

		this.username = username;
		this.password = password;
		this.roles = roles;
	}
	*/
	
}
