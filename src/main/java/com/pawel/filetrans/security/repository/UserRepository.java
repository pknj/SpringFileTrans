package com.pawel.filetrans.security.repository;

import com.pawel.filetrans.model.security.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);
}
