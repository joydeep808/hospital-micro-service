package com.example.auth_service.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.auth_service.dto.user.UserLoginDTO;
import com.example.auth_service.dto.user.UserRegisterDto;
import com.example.auth_service.entity.UserEntity;
import com.example.auth_service.entity.UserExtra;
import com.example.auth_service.jwt.JwtUtil;
import com.example.auth_service.mapping.user.UserLoginMapping;
import com.example.auth_service.repository.UserExtraRepo;
import com.example.auth_service.repository.UserRepo;
import com.example.auth_service.util.Response;

import jakarta.servlet.http.*;
import jakarta.transaction.Transactional;

@Service
public class UserService {
  private final UserRepo userRepo;
  private final PasswordEncoder passwordEncoder;
  private final UserExtraRepo userExtraRepo;
  private final JwtUtil jwtUtil;

  @Autowired
  public UserService(UserRepo userRepo , PasswordEncoder passwordEncoder , UserExtraRepo userExtraRepo , JwtUtil jwtUtil){
    this.jwtUtil = jwtUtil;
    this.userExtraRepo = userExtraRepo;
    this.passwordEncoder = passwordEncoder;
    this.userRepo = userRepo;
  }
  @Transactional
  public ResponseEntity<Response> registerUser(UserRegisterDto user){
    Response response = new Response();
    String isUserAlreadyExist = userRepo.findByUserEmail(user.getEmail()).orElse(null);
    if (isUserAlreadyExist != null) {
        response.sendErrorResponse("Email already register with us", 400);
        return response.sendApiResponse(response);
    }
    UserEntity userDetails = new UserEntity();
    userDetails.setEmail(user.getEmail());
    userDetails.setPassword(passwordEncoder.encode(user.getPassword()));
    userDetails.setName(user.getName());

   Long Longid =  userRepo.save(userDetails).getId();
    UserExtra userExtra = new UserExtra();
    userExtra.setId(Longid);
    userExtra.setDob(user.getDob());
    userExtraRepo.save(userExtra);
    response.sendSuccessResponse("User saved successfully done", 201 ,Longid);
    return response.sendApiResponse(response);
  }
  @Transactional
  public ResponseEntity<Response> loginUser(UserLoginDTO userLoginDTO ,  HttpServletResponse httpResponse){
    Response response = new Response();
    UserLoginMapping details = userRepo.userloginRequiredDetails(userLoginDTO.getEmail()).orElse(null);
    int passwordCounter = details.getPasswordRetryCounter();
    String password = details.getPassword();
    Long id = details.getId();
    if (passwordCounter <= 0 && details.getAccountLockedTime().isAfter(LocalDateTime.now())) {
      int time=  details.getAccountLockedTime().getHour() - LocalDateTime.now().getHour();
      response.sendErrorResponse("Please retry after "+ time +" Hours", 400);
      return response.sendApiResponse(response);
    }
    if (!passwordEncoder.matches(userLoginDTO.getPassword(), password)) {
     
      if (passwordCounter  == 1) {
          userExtraRepo.updateIncorrectPasswordDetails(id, 0,LocalDateTime.now().plusHours(12) );
      }
     else  userExtraRepo.updateIncorrectPasswordDetails(id,  - 1,LocalDateTime.now().plusHours(12) );
     response.sendErrorResponse("incorrect password " + (passwordCounter -1 ) +" retries are there  " ,400);
     return response.sendApiResponse(response);
    }
    String accessToken = jwtUtil.generateAccessToken( userLoginDTO.getEmail(), details.getRole() , id);
    String refreshToken = jwtUtil.generateRefreshToken(id);
    Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
    accessTokenCookie.setPath("/");
    accessTokenCookie.setMaxAge(30 * 60);
    Cookie refreshTokenCookie=  new Cookie("refreshToken", refreshToken);
    refreshTokenCookie.setPath("/");
    refreshTokenCookie.setMaxAge(24 * 60 * 60 * 60);
    userExtraRepo.setRefreshToken(id, refreshToken, LocalDateTime.now().plusDays(60));
    httpResponse.addCookie(refreshTokenCookie);
    httpResponse.addCookie(accessTokenCookie);
    response.sendSuccessResponse("Login Successfully done", 200, refreshTokenCookie);
    return response.sendApiResponse(response);
  }
}
