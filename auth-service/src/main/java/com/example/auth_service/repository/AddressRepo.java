package com.example.auth_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.auth_service.entity.Address;

@Repository
public interface AddressRepo extends JpaRepository<Address , Long> {
  
  
}
