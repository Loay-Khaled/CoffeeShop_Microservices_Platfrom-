package com.coffeeshop.loyaltyservice.repo;

import com.coffeeshop.loyaltyservice.model.LoyaltyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoyaltyTransactionRepository extends JpaRepository<LoyaltyTransaction, Long> {
    List<LoyaltyTransaction> findByAccountIdOrderByCreatedAtDesc(Long accountId);
    List<LoyaltyTransaction> findByOrderId(Long orderId);
}
