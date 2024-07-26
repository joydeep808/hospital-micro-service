package com.example.auth_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.auth_service.entity.UserEntity;
import com.example.auth_service.repository.UserRepo;


@Service
public class UserDetailsServiceImpl implements UserDetailsService{

  private final UserRepo userRepo;
  @Autowired
  public UserDetailsServiceImpl(UserRepo userRepo){
    this.userRepo = userRepo;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
   UserEntity user = userRepo.findByEmail(email).orElse(null);
   if (user== null) {
      throw new UsernameNotFoundException("User not found");
   }
   else return user;

  }
  
}
