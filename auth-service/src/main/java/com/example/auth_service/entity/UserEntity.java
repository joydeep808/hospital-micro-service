package com.example.auth_service.entity;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@Data
@AllArgsConstructor
@Builder
@Entity
public class UserEntity  implements UserDetails{
 

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull(message = "Name is required")
  private String name;
  @Column(unique = true , nullable=false )
  @NotNull(message = "Email is required")
  @Length(min = 6 , max = 100 , message = "email should contain atleast 6 digits to 100 digits")
  private String email;
  @NotNull(message = "Password is required")
  private String password;
  private  String role;
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime createdAt;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.stream(role.split(",")).map(r-> new SimpleGrantedAuthority(r)).collect(Collectors.toList());
  }
  @Override
  public String getPassword() {
      return password;
  }
  @Override
  public String getUsername() {
    return email;
  }
  public UserEntity(){
    this.createdAt = LocalDateTime.now();
    this.role = "USER,ADMIN";
  }
 

}
