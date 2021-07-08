package com.adidas.test.pubservice.common.error;

public class InvalidTokenException extends RuntimeException {

  public InvalidTokenException() {
    super("Token not valid");
  }

  public InvalidTokenException(String msg) {
    super(msg);
  }

}