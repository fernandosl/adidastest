package com.adidas.test.pubservice.common.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public abstract class BaseRestController {

  protected static final String PARAM_PAGE_PAGE = "_page";
  protected static final String PARAM_PAGE_ROWS = "_rows";
  protected static final String PARAM_PAGE_SORT = "_sort";
  protected static final String PARAM_PAGE_DIRECTION = "_direction";
  protected static final String SORT_ASCENDING = "asc";
  protected static final String SORT_DESCENDING = "desc";

  public BaseRestController() {
  }
}