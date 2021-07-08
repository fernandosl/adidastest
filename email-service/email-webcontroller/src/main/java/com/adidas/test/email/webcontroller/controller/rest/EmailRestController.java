package com.adidas.test.email.webcontroller.controller.rest;

import com.adidas.test.email.domain.integration.EmailSendService;
import com.adidas.test.email.model.dto.EmailDTO;
import com.adidas.test.email.webcontroller.util.ApplicationConstants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApplicationConstants.API_VERSION + "/email")
public class EmailRestController {

  @Autowired
  EmailSendService emailSendService;

  private static final Logger LOG = LoggerFactory.getLogger(EmailRestController.class.getName());

  /**
   * Creates a email send request, the petition is stored in local DB and processed later to be sent
   * by asincronous task
   *
   * @param emailDTO object with the email content
   * @return emailDTO object updated with the proccess
   */
  @PreAuthorize("hasRole('ROLE_EMAIL_SEND_MESSAGE')")
  @PostMapping(
      value = "/send",
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @ApiOperation(value = "sendMessage", notes = "Tries to send email, adds send date to object if message is sent")
  @ResponseStatus(value = HttpStatus.OK)
  @ResponseBody
  public ResponseEntity<EmailDTO> sendMessage(
      @ApiParam(value = "Object with the email to be sent", required = true) @Valid @RequestBody(required = true) final EmailDTO emailDTO
  ) {

    Optional<EmailDTO> emailDTOResponse = Optional.empty();
    try {
      // tries to send email
      emailDTOResponse = Optional.of(emailSendService.sendEmail(emailDTO));
    } catch (Exception e) {
      LOG.error("Error sending email to " + emailDTO.getEmail(), e);
    }
    if (emailDTOResponse.isPresent()) {
      return new ResponseEntity<>(emailDTOResponse.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
  }
}
