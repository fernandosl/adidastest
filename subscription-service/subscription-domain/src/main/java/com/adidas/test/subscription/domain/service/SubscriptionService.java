package com.adidas.test.subscription.domain.service;

import com.adidas.test.subscription.domain.dto.SubscriptionDTO;
import com.adidas.test.subscription.domain.entity.SubscriptionEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubscriptionService {

  public Boolean cancelSubscriptionById(Long subscriptionId);

  public Optional<SubscriptionEntity> createSubscription(SubscriptionEntity subscriptionEntity);

  public Optional<SubscriptionEntity> getSubscriptionById(Long id);

  public Page<SubscriptionEntity> listSubscriptions(SubscriptionDTO subscriptionDTO, Pageable pageable);

}
