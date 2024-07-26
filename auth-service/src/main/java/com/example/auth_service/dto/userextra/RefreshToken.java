package com.example.auth_service.dto.userextra;

import java.time.LocalDateTime;

public interface RefreshToken {
  String getRefreshToken();
  LocalDateTime getRefreshTokenValidity();  
}
