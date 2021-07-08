package com.adidas.test.pubservice.domain.dto;

import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class SubscriptionDTO implements Serializable {

  private Long id;

  private java.util.Date creationDate;

  @Email(message = "Email is not valid")
  private String email;

  @Size(min = 4, max = 80, message = "Name must be between 4 and 80 chars")
  private String firstName;

  @Size(max = 80, message = "Surname 1 must be max 80 chars")
  private String surname1;

  @Size(max = 80, message = "Surname 2 must be max 80 chars")
  private String surname2;

  private String gender;

  @NotNull(message = "Birth Date must be provided")
  private java.util.Date birthDate;

  @NotNull(message = "Consent must be accepted")
  private String consentFlag;

  @NotNull(message = "Newsletter campaign id must be provided")
  private String newslettterId;

}
