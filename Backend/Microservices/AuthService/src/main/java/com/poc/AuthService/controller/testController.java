package com.poc.AuthService.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.poc.AuthService.model.User;
import com.poc.AuthService.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class testController {
	
    private final UserService userService;
	@PostMapping("/user/save")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<User> testRoutes(@Valid @RequestBody User user)
	{
		System.out.println("function testroutes called");
		return new ResponseEntity<User>(userService.saveUser(user), HttpStatus.CREATED);
	}
	
	@GetMapping("/users/{userName}") 
	public ResponseEntity<User> getUserName(@PathVariable String userName)
	{
		System.out.println(userName);
		System.out.println(userService.getUser(userName));
		return ResponseEntity.ok().body(userService.getUser(userName));
	}
	


}
