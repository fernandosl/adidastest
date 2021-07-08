package com.adidas.test.pubservice.common.web.util;

import org.springframework.web.filter.CommonsRequestLoggingFilter;

public class CustomRequestLoggingFilter extends CommonsRequestLoggingFilter {

  public CustomRequestLoggingFilter() {
    //super.setMaxPayLoadLength(2000);
    //super.setIncludePayLoad(false); // DO NOT INCLUDE PAYLOAD!!!
    super.setIncludeClientInfo(true);
    super.setIncludePayload(false);
    super.setIncludeQueryString(true);
    super.setIncludeHeaders(false);
  }
}