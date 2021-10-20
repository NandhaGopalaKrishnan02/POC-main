package com.poc.AuthService.advice;

import java.util.Date;
import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data  @NoArgsConstructor
public class ErrorDetails {

	private Date timeStamp;
	private String errorMessage;
	private HttpStatus errorCode;
	private Map<String, String> errors;
	
	public ErrorDetails(Date timeStamp, String errorMessage, HttpStatus errorCode) {
		this.timeStamp = timeStamp;
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}
	
	public ErrorDetails(Date timeStamp, HttpStatus errorCode, Map<String, String> errors) {
		this.timeStamp = timeStamp;
		this.errors= errors;
		this.errorCode = errorCode;
	}
	
	
	
	
}
