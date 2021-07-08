package com.adidas.test.email.webcontroller.config;

import com.adidas.test.pubservice.common.web.util.CustomRequestLoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class AppConfig {

  @Bean
  public CommonsRequestLoggingFilter requestLoggingFilter() {
    System.out.println(" --- CONFIGURING COMMONS REQUEST LOGGING --- ");
    return new CustomRequestLoggingFilter();
  }

}