package com.adidas.test.subscription.webcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.WebApplicationInitializer;

@SpringBootApplication(scanBasePackages = {"com.adidas.test"})
@EnableScheduling
public class SubscriptionApplication extends SpringBootServletInitializer implements
    WebApplicationInitializer {

  private static final Logger LOG = LoggerFactory.getLogger(SubscriptionApplication.class.getName());

  @Autowired
  private Environment env;

  public static void main(String[] args) {
    SpringApplication.run(SubscriptionApplication.class, args);
  }

  @EventListener(ApplicationStartedEvent.class)
  void contextStartedEvent() {
    LOG.info("APP EVENT >> Started Event");
  }

  @EventListener(ApplicationStartingEvent.class)
  void contextRefreshedEvent() {
    LOG.info("APP EVENT >> Starting Event");
  }

  @EventListener(ApplicationReadyEvent.class)
  public void afterApplicationStartup() {
    String activeProfiles = env.getProperty("spring.profiles.active");
    LOG.info("APP EVENT >> Ready Event >> Profiles: " + activeProfiles);
  }
}
