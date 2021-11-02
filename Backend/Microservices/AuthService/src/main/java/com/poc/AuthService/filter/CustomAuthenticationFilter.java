package com.poc.AuthService.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.AuthService.JWTUtility.JWTUtility;
import com.poc.AuthService.advice.ErrorDetails;
import com.poc.AuthService.advice.ErrorMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private  final AuthenticationManager authenticationManager;
	

	

	
	public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	
		
}
	
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		log.info("UserName : {}", userName);
		log.info("Password : {}", password);
	
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
		return authenticationManager.authenticate(authenticationToken);
	}


	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		
		// Get successfully logged user info
		User user = (User)authentication.getPrincipal();
		
		// set content type for response
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		Map<String, String> res=new HashMap<>();
		
		// convert roles of type "GrantedAuthority" to String
		List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
		
		// Create access and refresh token
		String accessToken = JWTUtility.createAccessToken(user.getUsername(), roles);
		String refreshToken  = JWTUtility.createRefreshToken(user.getUsername());
		
		// set the response
		res.put("access_token", accessToken);
		res.put("refresh_token", refreshToken);
		new ObjectMapper().writeValue(response.getOutputStream(),res);
		
	}


	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		log.info("Unsuccessful authentication");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ErrorMessage.BAD_CREDENTIALS, HttpStatus.BAD_REQUEST);
		new ObjectMapper().writeValue(response.getOutputStream(),errorDetails);
	}
   
	
	


}
