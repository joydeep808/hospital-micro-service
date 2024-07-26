package com.example.auth_service.dto.user;

import java.time.LocalDateTime;

import com.example.auth_service.serialize.LocalDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterDto {
  private String password;
  private String email;
  private String name;
  @JsonFormat(pattern = "yyyy-mm-dd")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime dob;
}
