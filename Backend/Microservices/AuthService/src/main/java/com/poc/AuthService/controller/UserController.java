package com.poc.AuthService.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.poc.AuthService.model.Role;
import com.poc.AuthService.model.User;
import com.poc.AuthService.model.UserValidator;
import com.poc.AuthService.repository.UserRepo;
import com.poc.AuthService.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
	
    private final UserService userService;

    private final UserRepo userRepo;
  
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
		
		//Get Authorization header
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		Map<String, String> res= new HashMap<>();
		
		// check refresh token present in the incoming request
		if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")) {
			
			// get the refresh token only
			String refreshToken  = authorizationHeader.substring(7);
			
			// verify the token
			String userName = JWTUtility.verifyRefreshToken(refreshToken);
			
			// Get the user details from db by using username which is present in refresh_token
			User user = userRepo.findByUserName(userName);
			
			// convert roles of type "ROLE" to String
			List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
			
			// create access token and refresh token 
			String newAccessToken = JWTUtility.createAccessToken(userName, roles);
			String newRefreshToken = JWTUtility.createRefreshToken(userName);
			
			// set the response
			res.put("access_token", newAccessToken);
			res.put("refresh_token", newRefreshToken);
		}
		
		
		return ResponseEntity.ok().body(res);
	}

}
