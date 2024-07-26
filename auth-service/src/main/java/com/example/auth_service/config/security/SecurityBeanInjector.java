package com.example.auth_service.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.auth_service.service.UserDetailsServiceImpl;


@Component
public class SecurityBeanInjector {
  @Autowired
  private final UserDetailsServiceImpl userDetailsServiceImpl;

  @Autowired
  public SecurityBeanInjector(UserDetailsServiceImpl userDetailsServiceImpl){
    this.userDetailsServiceImpl = userDetailsServiceImpl;
  }
  
  @Bean
  public AuthenticationProvider authenticationProvider(){
    DaoAuthenticationProvider daoAuthenticationProvider  = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(userDetailsServiceImpl);
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    return daoAuthenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
    return new  ProviderManager(authenticationProvider());
  }

  @Bean
  private PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


}
