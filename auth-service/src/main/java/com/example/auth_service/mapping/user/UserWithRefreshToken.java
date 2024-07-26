package com.example.auth_service.mapping.user;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserWithRefreshToken {
  Optional<String> getEmail();
  Optional<Long> getId();
  Optional<String> getRole();
  Optional<String> getRefreshToken();
  Optional<LocalDateTime> getRefreshTokenValidity();
  Optional<String> getPassword();
}
