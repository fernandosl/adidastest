package com.adidas.test.pubservice.common.model.security;

import lombok.Data;

@Data
public class TokenUser {

  private String username;

  private String password;

  private String[] permissions;
}
