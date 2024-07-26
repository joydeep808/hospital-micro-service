package com.example.auth_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.auth_service.entity.UserEntity;
import com.example.auth_service.mapping.user.UserLoginMapping;
import com.example.auth_service.mapping.user.UserWithRefreshToken;

@Repository
public interface UserRepo extends JpaRepository<UserEntity , Object>{
    @Query(value = "select * from user_entity where email =:email", nativeQuery = true)
    Optional<UserEntity> findByEmail(@Param("email") String email);

    @Query(value = "select u.email  from user_entity as u where u.email =:email" , nativeQuery = true)
    Optional<String> findByUserEmail(@Param("email") String email);
    @Query(value = "select u.password ,u.role,  u.id , ue.password_retry_counter , ue.account_locked_time from user_entity as u left join user_extra as ue on ue.user_id = u.id where u.email =:email " , nativeQuery =  true)
    Optional<UserLoginMapping> userloginRequiredDetails(@Param("email" ) String email);

    @Query(value = "select u.email , u.password , u.id , u.role , ue.refresh_token, ue.refresh_token_validity from user_entity as u left join user_extra as ue on ue.user_id = u.id where u.id = :id" , nativeQuery = true)
    UserWithRefreshToken userWithRefreshToken(@Param("id") Long id); 


}
