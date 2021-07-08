package com.adidas.test.pubservice.common.error;

import java.util.List;

public class ApplicationException extends Exception {

  private int code;
  private String status;
  private String detail;
  private String debug;
  private List<String> errors;

  public ApplicationException(String message) {
    super(message);
  }

  public ApplicationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ApplicationException(String message, int code) {
    super(message);
    this.code = code;
  }

  public ApplicationException(String message, Throwable cause, int code) {
    super(message, cause);
    this.code = code;
    if(cause != null) {
      this.detail = cause.getLocalizedMessage();
    }
  }

  public ApplicationException(String message, Throwable cause, int code, String status,
      String detail,
      String debug, List<String> errors) {
    super(message, cause);
    this.code = code;
    this.status = status;
    this.detail = detail;
    this.debug = debug;
    this.errors = errors;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public String getDebug() {
    return debug;
  }

  public void setDebug(String debug) {
    this.debug = debug;
  }

  public List<String> getErrors() {
    return errors;
  }

  public void setErrors(List<String> errors) {
    this.errors = errors;
  }
}
