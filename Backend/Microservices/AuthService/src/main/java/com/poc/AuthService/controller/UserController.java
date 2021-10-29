package com.poc.AuthService.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poc.AuthService.JWTUtility.JWTUtility;
import com.poc.AuthService.model.UserValidator;
import com.poc.AuthService.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
	
    private final UserService userService;
	@PostMapping("/user/save")
	public ResponseEntity<?> createUser(@Valid @RequestBody UserValidator user) throws Exception
	{
		Map<String,String> res = userService.saveUser(user);
		userService.addRoleToUser(user.getUserName(), user.getRolesToUser());
		return new ResponseEntity<Object>(res, HttpStatus.CREATED);
	}
	
	@GetMapping("/users/{userName}") 
	public ResponseEntity<?> getUser(@PathVariable String userName) throws Exception
	{
		return ResponseEntity.ok().body(userService.getUser(userName));
	}
	
	@GetMapping("/token/refresh_token")
	public ResponseEntity<?> getRefreshToken(HttpServletRequest request, HttpServletResponse response){
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		Map<String, String> res= new HashMap<>();
		if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")) {
			String refreshToken  = authorizationHeader.substring(7);
			JWTUtility jwtUtility = new JWTUtility();
			String newAccessToken = jwtUtility.verifyRefreshTokenAndCreateAccessToken(refreshToken);
			System.out.println(newAccessToken);
			res.put("access_token", newAccessToken);
		}
		
	
		return ResponseEntity.ok().body(res);
	}

}
