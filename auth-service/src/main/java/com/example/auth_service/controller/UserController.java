package com.example.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth_service.dto.user.UserLoginDTO;
import com.example.auth_service.dto.user.UserRegisterDto;
import com.example.auth_service.service.UserService;
import com.example.auth_service.util.Response;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
  
  private final UserService userService;
  public UserController(UserService userService){
    this.userService = userService;
  }
  @PostMapping("/auth/register")
  public ResponseEntity<Response> registerUser (@RequestBody @Valid UserRegisterDto  userRegisterDto){
      return userService.registerUser(userRegisterDto);
  }
  @PostMapping("/auth/login")
  public ResponseEntity<Response> loginUser(@RequestBody @Valid UserLoginDTO loginDTO , HttpServletResponse httpServletResponse){
    return userService.loginUser(loginDTO,httpServletResponse );
  }
  @GetMapping("/home")
  public String home(){
    return "HomePage";
  }
  @GetMapping("/access")
  public String access(){
    return "AccessPage";
  }
}
