package com.poc.AuthService.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.poc.AuthService.filter.CustomAuthenticationFilter;
import com.poc.AuthService.filter.CustomAuthorizationFilter;

import lombok.RequiredArgsConstructor;


@EnableWebSecurity @Configuration @RequiredArgsConstructor 
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	private final UserDetailsService userDetailsService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@Override 
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}
		
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(),getApplicationContext());
		customAuthenticationFilter.setFilterProcessesUrl("/api/v1/login");
		http.csrf().disable();
		http.cors();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers("/api/v1/login", "/api/v1/token/refresh_token","/api/v1/logout").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/user/**").hasAnyAuthority("ROLE_USER");
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/user/save/**").hasAnyAuthority("ROLE_ADMIN");
		
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilter(customAuthenticationFilter);
		http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
	
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		
		return super.authenticationManagerBean();
	}
	

	  @Bean
	    CorsConfigurationSource corsConfigurationSource() 
	    {
	        CorsConfiguration configuration = new CorsConfiguration();
	        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
	        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration);
	        return source;
	    }
	
	

}
