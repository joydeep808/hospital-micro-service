package com.example.auth_service.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.auth_service.dto.userextra.RefreshToken;
import com.example.auth_service.entity.UserExtra;

import io.lettuce.core.dynamic.annotation.Param;


@Repository
public interface UserExtraRepo extends JpaRepository<UserExtra , Long>{
  
  @Query(value = "select u.refresh_token ,u.refresh_token_validity from user_extra as u where u.user_id =:id", nativeQuery =true)
  RefreshToken refreshTokenDetails(@RequestParam("id") Long id );

  @Modifying
@Query(value = "update user_extra u set u.password_retry_counter = :counter, u.account_locked_time = :lockedTime where u.user_id = :id", nativeQuery = true)
void updateIncorrectPasswordDetails(@Param("id") Long id, @Param("counter") int counter, @Param("lockedTime") LocalDateTime lockedTime);
  @Modifying 
  @Query(value = "update user_extra set refresh_token = :token , refresh_token_validity = :time where user_id = :id" , nativeQuery = true)
  void setRefreshToken (@Param("id") Long id , @Param("token") String token , @Param("time") LocalDateTime time);

}
