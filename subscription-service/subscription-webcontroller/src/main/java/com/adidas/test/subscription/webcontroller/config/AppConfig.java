package com.adidas.test.subscription.webcontroller.config;

import com.adidas.test.pubservice.common.web.util.CustomRequestLoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class AppConfig {

  private static final Logger LOG = LoggerFactory.getLogger(AppConfig.class.getName());

  @Bean
  public CommonsRequestLoggingFilter requestLoggingFilter() {
    System.out.println(" --- CONFIGURING COMMONS REQUEST LOGGING --- ");
    return new CustomRequestLoggingFilter();
  }

  @Bean
  public RestTemplate restTemplate() {
    LOG.info(" --- CONFIGURING REST TEMPLATE ---");
    RestTemplate restTemplate = new RestTemplate();
    /*try {
      List<ClientHttpRequestInterceptor> interceptors
          = restTemplate.getInterceptors();
      if (CollectionUtils.isEmpty(interceptors)) {
        interceptors = new ArrayList<>();
      }
      interceptors.add(new RestTemplateHeaderModifierInterceptor());
      restTemplate.setInterceptors(interceptors);
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error("Error configuring restTemplate", e);
    }*/
    return restTemplate;
  }

}