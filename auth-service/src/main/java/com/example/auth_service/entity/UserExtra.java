package com.example.auth_service.entity;

import java.time.LocalDateTime;

import com.example.auth_service.serialize.LocalDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;



@Entity
@Data
@AllArgsConstructor
@Builder
public class UserExtra  {
  @Id
  @Column(name = "user_id" , unique=true , nullable = false)
  private Long id;  
  @Temporal(TemporalType.TIMESTAMP)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @Column(columnDefinition = "TIMESTAMP")
  @JsonFormat(pattern="yyyy-mm-dd")
  private LocalDateTime dob;
  private String refreshToken;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(columnDefinition = "TIMESTAMP")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime refreshTokenValidity;
  private String otp;
  private int passwordRetryCounter;
  private LocalDateTime accountLockedTime;
  public UserExtra(){
    this.accountLockedTime = LocalDateTime.now();
    this.passwordRetryCounter = 5;
  }
} 
