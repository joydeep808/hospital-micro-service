package com.example.auth_service.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
  @Id
  @Column(name="user_id" , nullable = false , unique = true)
  private Long id;
  private String streetName;
  private String state;
  private String city;
  private String landmark;
  private Long pincode;
}
