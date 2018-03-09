package com.pawel.springfiletrans.security.repository;

import com.pawel.springfiletrans.model.security.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);
}
