package com.example.auth_service.jwt;

import java.util.*;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import com.example.auth_service.mapping.token.AccessTokenInformation;
import io.jsonwebtoken.*;

@Component
public class JwtUtil {
  
  private SecretKey key;
  // private static final long expirationTime =600000 * 6 * 24 * 600;

  public JwtUtil(){
    String secretKey = "klhsldkfhskdhfkjsdfklshdfl";
    byte[] keyBytes = Base64.getEncoder().encode(secretKey.getBytes());
    this.key = new SecretKeySpec(keyBytes, "HmacSHA256"); // Key is ready

  }

  public String generateAccessToken(String email  , String role , Long id){
    String token =  Jwts.builder().subject(email).claim("id" , id).claim("email" , email).claim("role", role).claim("authorities", Arrays.stream(role.split(",")).map(r->new SimpleGrantedAuthority(r)).collect(Collectors.toList()) ).issuedAt(new Date(System.currentTimeMillis())).expiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000)).signWith(key).compact();
    return token;

  }
  public String generateRefreshToken(Long id){
    return Jwts.builder().claim("id", id).issuedAt(new Date(System.currentTimeMillis())).expiration(new Date(System.currentTimeMillis()+ 60 * 60 * 1000 * 1000 * 60)).signWith(key).compact();
  }

  public Claims extractClaims(String token) throws Exception{
   try {
    
     return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
     
   } catch (Exception e) {
      throw new Exception("Please give the valid token");
   }
  }

  public AccessTokenInformation getAccessTokenInformation(String token) throws Exception{
    Claims claims = extractClaims(token);
    return new AccessTokenInformation((int) claims.get("id"),(String) claims.get("email"),claims.getExpiration(),(String) claims.get("role"));

  }
  public Long extractId(String token) throws Exception{
    Claims claims  =  extractClaims(token);
    return (long) claims.get("id");
  }
}
