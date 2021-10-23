package com.poc.AuthService.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Data
@AllArgsConstructor @NoArgsConstructor

public class UserRowCount {
	@Id
	private Long id;
	private int userRowCount;
}