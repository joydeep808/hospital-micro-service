package com.example.auth_service.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoForJwt {

  private Long id;
  private String name;
  private String email;
  private String role;
  
}