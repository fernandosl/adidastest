package com.adidas.test.pubservice.domain.service;

import com.adidas.test.pubservice.domain.dto.SubscriptionDTO;
import org.springframework.data.domain.Page;

public interface SubscriptionClientService {

  public Boolean cancelSubscriptionById(Long subscriptionId, String bearer);

  public SubscriptionDTO createSubscription(SubscriptionDTO subscriptionDTO, String bearer);

  public SubscriptionDTO getSubscriptionById(Long id, String bearer);

  public Page<SubscriptionDTO> listSubscriptions(SubscriptionDTO subscriptionDTO,
      String _page, String _rows, String _sort, String _direction, String bearer);

}
