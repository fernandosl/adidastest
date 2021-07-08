package com.adidas.test.subscription.webcontroller.config;

import com.adidas.test.subscription.domain.process.SendEmailProcess;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulerConfig {

  @Bean
  public SendEmailProcess scheduledSendEmailProcessTask() {
    return new SendEmailProcess();
  }
}
