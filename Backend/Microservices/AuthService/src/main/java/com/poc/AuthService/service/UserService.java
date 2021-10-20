package com.poc.AuthService.service;

import java.util.List;
import java.util.Optional;

import com.poc.AuthService.model.User;

public interface UserService {
	
	User saveUser(User user);
	void addRoleToUser(String userName, String roleName);
	User getUser(String userName);
	
	

}
