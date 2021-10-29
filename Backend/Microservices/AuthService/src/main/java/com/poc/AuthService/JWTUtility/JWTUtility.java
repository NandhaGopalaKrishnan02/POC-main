package com.poc.AuthService.JWTUtility;



import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.poc.AuthService.model.Role;
import com.poc.AuthService.model.User;
import com.poc.AuthService.repository.UserRepo;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class JWTUtility {
	
	private   static String SECRET_KEY = "SecretPOCkey";
	private   static Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
	private   static int ACCESS_TOKEN_EXP_TIME = 6*60*1000;
	private   static String ISSUER = "AUTH_SERVICE";
	private final UserRepo userRepo;
	

	
	
	public static String createAccessToken (String userName, List<String> roles) {
		return JWT.create()
				.withSubject(userName)
				.withIssuedAt(new Date(System.currentTimeMillis()))
				.withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXP_TIME))
				.withIssuer(ISSUER)
				.withClaim("roles", roles)
				.sign(algorithm);
	}
	
	public static String createRefreshToken (String userName) {
		return JWT.create()
				.withSubject(userName)
				.withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXP_TIME))
				.withIssuer(ISSUER)
				.sign(algorithm);
	}
	
	
	public  String verifyRefreshTokenAndCreateAccessToken(String refreshToken) {
		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT decodedJWT = verifier.verify(refreshToken);
		System.out.println(decodedJWT);
		String username = decodedJWT.getSubject();
		User user = userRepo.findByUserName(username);
		List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
		return createAccessToken(username, roles);
	}
	
	
	
}
