package com.pawel.filetrans.security;

import java.io.Serializable;

import lombok.Data;

@Data
public class JwtAuthenticationRequest implements Serializable {

  private static final long serialVersionUID = -8445943548997154878L;

  private String email;
  private String username;
  private String password;

  public JwtAuthenticationRequest() {
    super();
  }

  public JwtAuthenticationRequest(String username, String password) {
    this.setUsername(username);
    this.setPassword(password);
  }
}