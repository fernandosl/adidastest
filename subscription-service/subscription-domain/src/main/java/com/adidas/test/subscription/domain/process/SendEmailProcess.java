package com.adidas.test.subscription.domain.process;


import com.adidas.test.email.model.dto.EmailDTO;
import com.adidas.test.pubservice.common.model.security.TokenUser;
import com.adidas.test.pubservice.common.security.JWTUtils;
import com.adidas.test.subscription.domain.service.SubscriptionService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class SendEmailProcess {

  private static final Logger LOG = LoggerFactory.getLogger(SendEmailProcess.class.getName());

  private static final String TASK_EMAIL_PROCESS = "task.email.process";

  private static final String ACTION_SEND_EMAIL = "/email/send";

  @Value(value = "${email.service.host}")
  private String emailServiceUrl;

  @Autowired
  SubscriptionService subscriptionService;

  @Value("${" + TASK_EMAIL_PROCESS + "}")
  private String cronEmailProcess;

  public String getCronEmailProcess() {
    return cronEmailProcess;
  }

  @Scheduled(cron = "${" + TASK_EMAIL_PROCESS + "}")
  public void processSendEmail() {
    LOG.info("Sending email!");
    // get pending send email subscriptions and send the email
    EmailDTO emailDTO = composeEmail();
    // generate bearer token for authentication on email uservice
    String bearer = generateTokenUser();
    EmailDTO emailDTOResponse = null;
    try {
      emailDTOResponse = sendEmail(emailDTO, bearer);
    }
    catch(Exception e) {
      LOG.error("Exception during email sending to " + emailDTO.getEmail(), e);
    }
    if(emailDTOResponse != null && emailDTOResponse.getSendDate() != null) {
      LOG.info("Email Sent!");
    } else {
      LOG.info("Email Failed!");
    }
  }

  private String generateTokenUser() {
    TokenUser user = new TokenUser();
    user.setUsername("SendEmailProcess");
    List<String> permissions = new ArrayList();
    permissions.add("ROLE_EMAIL_SEND_MESSAGE");
    user.setPermissions((String[])permissions.toArray(new String[0]));
    return JWTUtils.codeToken(user);
  }

  protected EmailDTO composeEmail() {
    EmailDTO emailDTO = new EmailDTO();
    emailDTO.setEmail("mockemail@notvalid.com");
    emailDTO.setSubject("Test subject");
    emailDTO.setContent("Test content");

    return emailDTO;
  }

  public EmailDTO sendEmail(EmailDTO emailDTO, String bearer) {
    HttpHeaders headers = createDefaultHeaders(bearer);

    HttpEntity<EmailDTO> entity = new HttpEntity<>(emailDTO, headers);

    UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
        .fromHttpUrl(emailServiceUrl)
        .path("/")
        .path(ACTION_SEND_EMAIL);

    ResponseEntity<EmailDTO> respEntity = restTemplate
        .exchange(uriComponentsBuilder.toUriString(), HttpMethod.POST, entity,
            EmailDTO.class);

    return respEntity.getBody();
  }

  @Autowired
  RestTemplate restTemplate;

  /**
   * Creates default headers por backend-backend call
   *
   * @param bearer jwt token for authentication
   * @return object with HttpHeaders
   */
  protected HttpHeaders createDefaultHeaders(String bearer) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    if (bearer != null) {
      if (bearer.startsWith("Bearer ")) {
        headers.set("jwttoken", bearer);
      } else {
        headers.set("jwttoken", "Bearer " + bearer);
      }
    }

    return headers;
  }

}
