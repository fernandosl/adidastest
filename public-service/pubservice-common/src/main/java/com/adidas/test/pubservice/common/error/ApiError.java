package com.adidas.test.pubservice.common.error;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Data
public class ApiError {

  private HttpStatus httpStatus;
  private String subCode;
  //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  private String timestamp;
  private String message;
  private int code;
  private String detail;
  private String debugMessage;
  private List<String> errors;

  @Getter(value = AccessLevel.NONE)
  @Setter(value = AccessLevel.NONE)
  private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

  private ApiError() {
    LocalDateTime now = LocalDateTime.now();
    timestamp = sdf.format(now);
  }

  public ApiError(HttpStatus httpStatus) {
    this();
    this.httpStatus = httpStatus;
  }

  public ApiError(HttpStatus httpStatus, String message, String detail, String debug,
      List<String> errors) {
    this();
    this.httpStatus = httpStatus;
    this.message = message;
    this.detail = detail;
    this.errors = errors;
    this.debugMessage = debug;
  }

  public ApiError(HttpStatus httpStatus, String message, String detail, String debug,
      List<String> errors, int code) {
    this();
    this.httpStatus = httpStatus;
    this.message = message;
    this.detail = detail;
    this.errors = errors;
    this.debugMessage = debug;
    this.code = code;
  }

}
