package com.example.auth_service.config.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.auth_service.dto.userextra.RefreshToken;
import com.example.auth_service.jwt.JwtUtil;
import com.example.auth_service.mapping.token.AccessTokenInformation;
import com.example.auth_service.mapping.user.UserWithRefreshToken;
import com.example.auth_service.repository.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JwtFilter extends OncePerRequestFilter{
  private final JwtUtil jwtUtil;
  private final UserRepo userRepo;
  private final UserExtraRepo userExtraRepo;
  @Autowired
  public JwtFilter(JwtUtil jwtUtil , UserRepo userRepo , UserExtraRepo userExtraRepo){
    this.userExtraRepo = userExtraRepo;
    this.userRepo = userRepo;
    this.jwtUtil = jwtUtil;
  }

  @Override 
  protected boolean shouldNotFilter(HttpServletRequest req) throws ServletException{
    String path = req.getRequestURI();
    return path.startsWith("/user/auth/register") || path.startsWith("/user/auth/login") || path.startsWith("/user/access");
  }
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException { 
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
          filterChain.doFilter(request, response);
          return;
        }
        Cookie[] cookies = request.getCookies();
        if (Arrays.equals(cookies, null) || cookies == null || List.of(cookies).isEmpty()) {
          filterChain.doFilter(request, response);
          return;
        }
        for (Cookie cookie : cookies) {
          if (cookie.getName().equals("accessToken")) {
            boolean isSuccess= getAccessFromAccessToken(cookie.getValue());
            if (isSuccess) {
              filterChain.doFilter(request, response);
              return;
            }
          }
          if(cookie.getName().equals("refreshToken")){
           boolean isSuccess;
          try {
            isSuccess = getAccessFromRefreshToken(cookie.getValue(), response , request);
            if (isSuccess) {
             filterChain.doFilter(request, response);
             return;
            }
          } catch (Exception e) {
            throw new IOException("Token not varifyed");
          }
          }
        }
      filterChain.doFilter(request, response);
      return;
  }

  public boolean checkRefreshTokenValidOrNot(String token) throws Exception{
    Long id = jwtUtil.extractId(token);
    RefreshToken refreshTokenDetails = userExtraRepo.refreshTokenDetails(id);
    if (refreshTokenDetails.getRefreshToken() == null || !refreshTokenDetails.getRefreshToken().equals(token) && refreshTokenDetails.getRefreshTokenValidity().isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))) {
      return false;
    }
    else return true;
  }
  public boolean getAccessFromAccessToken(String token){
    try {
      AccessTokenInformation accessTokenInformation = jwtUtil.getAccessTokenInformation(token);
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(accessTokenInformation , null , accessTokenInformation.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authToken);
    return true;
    } catch (Exception e) {
      System.out.println(e.getLocalizedMessage());
      return false;
    }
  }
  public boolean getAccessFromRefreshToken(String token , HttpServletResponse response , HttpServletRequest request) throws Exception{
    Long id = jwtUtil.extractId(token);
    UserWithRefreshToken user = userRepo.userWithRefreshToken(id);
    if (user  == null || !user.getId().isPresent()) {
        return false;
    }
    LocalDateTime refreshTokenTime = user.getRefreshTokenValidity().get();
    if (refreshTokenTime.isBefore(LocalDateTime.now())) {
      return false;
    }
    String eamil  = user.getEmail().get();
    String[] role = user.getRole().get().split(",");
    Collection<? extends GrantedAuthority> authorities =Arrays.stream(role).map(r-> new SimpleGrantedAuthority(r)).toList();
    String accessToken = jwtUtil.generateAccessToken(eamil, user.getRole().get() , id);
    Cookie newAccessToken = new Cookie("accessToken" , accessToken);
    newAccessToken.setPath("/");
    newAccessToken.setMaxAge(60 * 30);
    response.addCookie(newAccessToken);
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id,user.getPassword().get() ,authorities);
    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    return true;
  }

 
}
