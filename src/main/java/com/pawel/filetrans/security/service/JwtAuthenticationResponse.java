package com.pawel.filetrans.security.service;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class JwtAuthenticationResponse implements Serializable {

  private static final long serialVersionUID = 1250166509972483573L;

  private final String token;

  public JwtAuthenticationResponse(String token) {
    this.token = token;
  }
}