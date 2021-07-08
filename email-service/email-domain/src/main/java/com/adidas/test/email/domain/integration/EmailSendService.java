package com.adidas.test.email.domain.integration;

import com.adidas.test.email.model.dto.EmailDTO;
import org.springframework.stereotype.Service;

@Service
public class EmailSendService {

  public EmailDTO sendEmail(EmailDTO emailDTO) {
    // mock to send email
    // mark email as sent randomly
    if (System.currentTimeMillis() % 10 == 0) {
      // one out of ten send fails
      emailDTO.setSendDate(null);
    } else {
      emailDTO.setSendDate(new java.util.Date());
    }
    return emailDTO;
  }
}
