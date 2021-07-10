package com.adidas.test.pubservice.common.web.util;

import com.adidas.test.pubservice.common.model.security.AuthUser;
import com.adidas.test.pubservice.common.security.JwtAuthenticationFilter;
import java.io.IOException;
import javax.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RestTemplateHeaderModifierInterceptor
    implements ClientHttpRequestInterceptor {

  private static final Logger LOG = LoggerFactory
      .getLogger(RestTemplateHeaderModifierInterceptor.class.getName());

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request,
      byte[] body,
      ClientHttpRequestExecution execution) throws IOException {

    LOG.info("Intercepting petition REST TEMPLATE " + request.getURI() != null ? request.getURI()
        .toString() : "-null-");

    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    Cookie[] cookies = ((ServletRequestAttributes) requestAttributes).getRequest().getCookies();

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if(!request.getHeaders().containsKey(JwtAuthenticationFilter.HEADER_STRING)) {
      if (authentication.getPrincipal() != null && authentication
          .getPrincipal() instanceof AuthUser) {
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        if (authUser.getJwtTokenHeader() != null
            && authUser.getJwtTokenHeader().trim().length() > 0) {
          request.getHeaders().add(JwtAuthenticationFilter.HEADER_STRING,
              JwtAuthenticationFilter.TOKEN_PREFIX + authUser.getJwtTokenHeader());
        }
      }
    }

    ClientHttpResponse response = execution.execute(request, body);
    return response;
  }
}