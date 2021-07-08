package com.adidas.test.subscription.domain.service.impl;

import com.adidas.test.subscription.domain.dto.SubscriptionDTO;
import com.adidas.test.subscription.domain.entity.QSubscriptionEntity;
import com.adidas.test.subscription.domain.entity.SubscriptionEntity;
import com.adidas.test.subscription.domain.repository.SubscriptionRepository;
import com.adidas.test.subscription.domain.service.SubscriptionService;
import com.querydsl.core.BooleanBuilder;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@Lazy
public class SubscriptionServiceImpl implements SubscriptionService {

  @Autowired
  SubscriptionRepository subscriptionRepository;

  @Transactional(readOnly = false)
  @Override
  public Boolean cancelSubscriptionById(Long subscriptionId) {
    Boolean cancelled = Boolean.FALSE;
    if (subscriptionRepository.existsById(subscriptionId)) {
      subscriptionRepository.updateCancelSubscription(subscriptionId, new java.util.Date());
      cancelled = Boolean.TRUE;
    }
    return cancelled;
  }

  @Transactional(readOnly = false)
  @Override
  public Optional<SubscriptionEntity> createSubscription(SubscriptionEntity subscriptionEntity) {
    SubscriptionEntity subscriptionEntityDB = subscriptionRepository.save(subscriptionEntity);
    return Optional.of(subscriptionEntityDB);
  }

  @Override
  public Optional<SubscriptionEntity> getSubscriptionById(Long id) {
    Optional<SubscriptionEntity> subscriptionEntity = subscriptionRepository.findById(id);
    return subscriptionEntity;
  }

  @Override
  public Page<SubscriptionEntity> listSubscriptions(SubscriptionDTO subscriptionDTO,
      Pageable pageable) {
    BooleanBuilder query = new BooleanBuilder();

    if (subscriptionDTO.getId() != null) {
      query.and(QSubscriptionEntity.subscriptionEntity.id.eq(subscriptionDTO.getId()));
    }

    if (subscriptionDTO.getEmail() != null) {
      query.and(QSubscriptionEntity.subscriptionEntity.email.eq(subscriptionDTO.getEmail()));
    }

    return subscriptionRepository.findAll(query, pageable);
  }
}
