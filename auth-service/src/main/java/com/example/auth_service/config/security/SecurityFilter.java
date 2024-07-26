package com.example.auth_service.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
public class SecurityFilter {
    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;

    @Autowired
    public SecurityFilter(JwtFilter jwtFilter, AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
        this.jwtFilter = jwtFilter;
    }
    @Bean
    public SecurityFilterChain chain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth->{
            auth.requestMatchers("/user/auth/**" , "/user/access").permitAll();
            auth.requestMatchers("/user/home").hasAnyAuthority("USER");
            auth.anyRequest().authenticated();
        })
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
  
    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    //     httpSecurity
    //     .authenticationProvider(authenticationProvider)
    //     .authorizeHttpRequests(request -> {
    //         request.requestMatchers("/user/auth/**" ,"/user/auth/*" , "/user/access").permitAll();
    //         request.requestMatchers("/user/home").hasAnyAuthority("USER").anyRequest().authenticated();
    //     })
    //     .csrf(csrf -> csrf.disable())
    //         .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    //         .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    //     return httpSecurity.build();
    // }



    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web)-> web.ignoring().requestMatchers("/user/auth/**" , "/user/access");
    }
}