package com.adidas.test.pubservice.domain.service.impl;

import com.adidas.test.pubservice.common.web.util.WebControllerUtils;
import com.adidas.test.pubservice.domain.dto.SubscriptionDTO;
import com.adidas.test.pubservice.domain.service.SubscriptionClientService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class SubscriptionClientServiceImpl implements SubscriptionClientService {

  @Value(value = "${subscription.service.host}")
  private String subscriptionServiceURL;

  private static final String ACTION_CANCEL_SUBSCRIPTION = "subscription/cancel/{subscriptionId}";
  private static final String ACTION_CREATE_SUBSCRIPTION = "subscription/create";
  private static final String ACTION_GET_SUBSCRIPTION = "subscription/get/{subscriptionId}";
  private static final String ACTION_LIST_SUBSCRIPTIONS = "subscription/list";

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

  /**
   * Interfaces client for cancel one subscription by its id
   *
   * @param subscriptionId
   * @param bearer
   * @return
   */
  @Override
  public Boolean cancelSubscriptionById(Long subscriptionId, String bearer) {
    HttpHeaders headers = createDefaultHeaders(bearer);

    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

    UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
        .fromHttpUrl(subscriptionServiceURL)
        .path("/")
        .path(ACTION_CANCEL_SUBSCRIPTION);

    Map<String, Object> urlParams = new HashMap<>();
    urlParams.put("subscriptionId", subscriptionId);

    String url = uriComponentsBuilder.buildAndExpand(urlParams).toUri().toString();

    ResponseEntity<Boolean> respEntityCancelSubscription = restTemplate
        .exchange(uriComponentsBuilder.buildAndExpand(urlParams).toUri(), HttpMethod.GET, entity,
            Boolean.class);

    Boolean boolResult = (Boolean) respEntityCancelSubscription
        .getBody();
    return boolResult;
  }

  /**
   * Interfaces client for create one subscription
   *
   * @param subscriptionDTO
   * @param bearer
   * @return
   */
  @Override
  public SubscriptionDTO createSubscription(SubscriptionDTO subscriptionDTO, String bearer) {
    HttpHeaders headers = createDefaultHeaders(bearer);

    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

    UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
        .fromHttpUrl(subscriptionServiceURL)
        .path("/")
        .path(ACTION_CREATE_SUBSCRIPTION);

    ResponseEntity<SubscriptionDTO> respEntity = restTemplate
        .exchange(uriComponentsBuilder.toUriString(), HttpMethod.POST, entity,
            SubscriptionDTO.class);

    return respEntity.getBody();
  }

  /**
   * Interfaces client for retrieving one subscription by its id
   *
   * @param subscriptionId
   * @param bearer
   * @return
   */
  @Override
  public SubscriptionDTO getSubscriptionById(Long subscriptionId, String bearer) {
    HttpHeaders headers = createDefaultHeaders(bearer);

    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

    UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
        .fromHttpUrl(subscriptionServiceURL)
        .path("/")
        .path(ACTION_GET_SUBSCRIPTION);

    Map<String, Object> urlParams = new HashMap<>();
    urlParams.put("subscriptionId", subscriptionId);

    String url = uriComponentsBuilder.buildAndExpand(urlParams).toUri().toString();

    ResponseEntity<SubscriptionDTO> respEntitySubscriptionDTO = restTemplate
        .exchange(uriComponentsBuilder.buildAndExpand(urlParams).toUri(), HttpMethod.GET, entity,
            SubscriptionDTO.class);

    SubscriptionDTO subscriptionDTO = (SubscriptionDTO) respEntitySubscriptionDTO
        .getBody();
    return subscriptionDTO;
  }

  @Override
  public Page<SubscriptionDTO> listSubscriptions(SubscriptionDTO subscriptionDTO, String _page,
      String _rows, String _sort, String _direction, String bearer) {
    HttpHeaders headers = createDefaultHeaders(bearer);

    HttpEntity<SubscriptionDTO> entity = new HttpEntity<>(subscriptionDTO, headers);

    UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
        .fromHttpUrl(subscriptionServiceURL)
        .path("/")
        .path(ACTION_LIST_SUBSCRIPTIONS);

    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    if (_page != null && StringUtils.isNumeric(_page)) {
      queryParams.add("_page", _page);
    }
    if (_rows != null && StringUtils.isNumeric(_rows)) {
      queryParams.add("_rows", _rows);
    }
    if (StringUtils.isNotEmpty(_sort)) {
      queryParams.add("_sort", _sort);
    }
    if (StringUtils.isNotEmpty(_direction)) {
      queryParams.add("_direction", _direction);
    }
    uriComponentsBuilder.queryParams(queryParams);

    String s = uriComponentsBuilder.buildAndExpand().toUriString();

    List<SubscriptionDTO> subscriptionDTOList = new ArrayList<>();

    ResponseEntity<List<SubscriptionDTO>> respEntity = restTemplate
        .exchange(uriComponentsBuilder.toUriString(), HttpMethod.POST, entity,
            new ParameterizedTypeReference<List<SubscriptionDTO>>() {});
    int totalRegisters = 0;
    try {
      HttpHeaders responseHeaders = respEntity.getHeaders();
      List<String> totalCountH = responseHeaders.get("X-Total-Count");
      if (totalCountH != null && totalCountH.size() > 0) {
        totalRegisters = Integer.parseInt(totalCountH.get(0));
      }
    }
    catch(Exception e) {
      totalRegisters = subscriptionDTOList.size();
    }


    Pageable pageable = WebControllerUtils.parsePageParams(_rows, _page, _sort, _direction);
    PageImpl<SubscriptionDTO> subscriptionDTOPage = new PageImpl<>(subscriptionDTOList, pageable,
        totalRegisters);
    return subscriptionDTOPage;
  }
}
