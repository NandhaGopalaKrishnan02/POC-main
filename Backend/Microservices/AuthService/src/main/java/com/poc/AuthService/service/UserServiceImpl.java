package com.poc.AuthService.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.poc.AuthService.exception.ResourceNotFoundException;
import com.poc.AuthService.model.User;
import com.poc.AuthService.repository.RoleRepo;
import com.poc.AuthService.repository.UserRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service @RequiredArgsConstructor @Slf4j @Transactional
public class UserServiceImpl implements UserService,UserDetailsService{
	
	final private UserRepo userRepo;
	final private RoleRepo roleRepo;
	
	
	@Override
	public User saveUser(User user) {
		user.setUserCreatedTime(new Date());
		return userRepo.save(user);
	}
	
	@Override
	public void addRoleToUser(String userName, String roleName) {
		
	}
	
	@Override
	public User getUser(String userName) {
		return userRepo.findByUserName(userName);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUserName(username);
		if(user == null) {
			log.error("User not found in the database");
			throw new UsernameNotFoundException(username);
		} else {
			log.info("User found in the database");
		} 
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().forEach(role ->{
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		});
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), authorities);
	}

	
	
	


	
	
	
	
}
