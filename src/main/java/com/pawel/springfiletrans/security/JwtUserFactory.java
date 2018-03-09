package com.pawel.springfiletrans.security;

import com.pawel.springfiletrans.model.security.Authority;
import com.pawel.springfiletrans.model.security.AuthorityName;
import com.pawel.springfiletrans.model.security.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

  private JwtUserFactory() {
  }

  public static JwtUser create(User user) {
    return new JwtUser(
        user.getId(),
        user.getUsername(),
        user.getFirstname(),
        user.getLastname(),
        user.getEmail(),
        user.getPassword(),
        mapToGrantedAuthorities(user.getAuthorities()),
        user.getEnabled(),
        user.getLastPasswordResetDate(),
        resolveAdmin(user)
    );
  }

  private static boolean resolveAdmin(User user) {
    return user.getAuthorities().stream().map(Authority::getName).anyMatch(r -> r == AuthorityName.ROLE_ADMIN);
  }

  private static List<GrantedAuthority> mapToGrantedAuthorities(List<Authority> authorities) {
    return authorities.stream()
        .map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
        .collect(Collectors.toList());
  }


}