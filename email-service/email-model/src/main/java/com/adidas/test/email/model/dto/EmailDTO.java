package com.adidas.test.email.model.dto;

import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class EmailDTO implements Serializable {

  private java.util.Date sendDate;

  @Email(message = "Email is not valid")
  private String email;

  @NotNull(message = "Must add email subject")
  private String subject;

  @NotNull(message = "Must add email content")
  private String content;

}
