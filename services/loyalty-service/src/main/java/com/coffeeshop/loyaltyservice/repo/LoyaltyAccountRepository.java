package com.coffeeshop.loyaltyservice.repo;

import com.coffeeshop.loyaltyservice.model.LoyaltyAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoyaltyAccountRepository extends JpaRepository<LoyaltyAccount, Long> {
    Optional<LoyaltyAccount> findByCustomerUsername(String username);
}
