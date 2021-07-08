package com.adidas.test.pubservice.common.web.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public final class WebControllerUtils {

  public static Pageable parsePageParams(String _rows, String _page, String _sort,
      String _direction) {
    Order order = null;
    if (_sort != null) {
      if ("desc".equalsIgnoreCase(_direction)) {
        order = Order.desc(_sort);
      } else {
        order = Order.asc(_sort);
      }
    }

    Sort sort = order != null ? Sort.by(new Order[]{order}) : Sort.unsorted();
    Pageable pageable = null;
    if (StringUtils.isNumeric(_rows)) {
      if (StringUtils.isNumeric(_page)) {
        pageable = PageRequest.of(Integer.parseInt(_page), Integer.parseInt(_rows), sort);
      } else {
        pageable = PageRequest.of(0, Integer.parseInt(_rows), sort);
      }
    } else {
      pageable = PageRequest.of(0, 2147483647, sort);
    }

    return pageable;
  }

  public static Pageable createEmptyPageable() {
    return WebControllerUtils.createEmptyPageable((Sort) null);
  }

  public static Pageable createEmptyPageable(Sort sort) {
    if (sort == null) {
      sort = Sort.unsorted();
    }

    return PageRequest.of(0, 2147483647, sort);
  }
}
