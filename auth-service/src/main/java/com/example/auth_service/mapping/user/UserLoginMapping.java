package com.example.auth_service.mapping.user;

import java.time.LocalDateTime;

public interface UserLoginMapping {
 Long getId();
 String getPassword();
 Integer getPasswordRetryCounter();
 LocalDateTime getAccountLockedTime();
  String getRole();
}
