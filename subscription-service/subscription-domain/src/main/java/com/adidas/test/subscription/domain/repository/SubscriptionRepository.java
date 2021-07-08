package com.adidas.test.subscription.domain.repository;

import com.adidas.test.subscription.domain.entity.SubscriptionEntity;
import java.util.Date;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends CrudRepository<SubscriptionEntity, Long>,
    QuerydslPredicateExecutor<SubscriptionEntity> {

  @Modifying
  @Query("Update SUBSCRIPTIONS s SET s.cancelDate=:cancelDate WHERE s.id=:subscriptionId")
  public void updateCancelSubscription(@Param("subscriptionId") Long subscriptionId, @Param("cancelDate") Date cancelDate);
}
