package com.example.auth_service.mapping.token;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccessTokenInformation {
  private String email;
  private Long id ;
  private String role;
  private Collection<? extends GrantedAuthority> authorities;
  private Date expiration;
  public AccessTokenInformation(int id , String email , Date expiration , String role ){
    this.email = email;
    this.id = (long) id;
    this.authorities  = Arrays.stream(role.split(",")).map(r-> new SimpleGrantedAuthority(r)).collect(Collectors.toList());
    this.expiration = expiration;
    this.role = role;

  }
}
