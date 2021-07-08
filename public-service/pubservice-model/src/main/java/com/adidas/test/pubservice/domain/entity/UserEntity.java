package com.adidas.test.pubservice.domain.entity;

import lombok.Data;

@Data
public class UserEntity {

  private Long id;
  private String username;
  private String password;
  private java.util.Date creationDate;
}
