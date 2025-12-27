package com.coffeeshop.loyaltyservice.service;

import com.coffeeshop.loyaltyservice.model.LoyaltyAccount;
import com.coffeeshop.loyaltyservice.model.LoyaltyTransaction;
import com.coffeeshop.loyaltyservice.repo.LoyaltyAccountRepository;
import com.coffeeshop.loyaltyservice.repo.LoyaltyTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LoyaltyService {

    private static final Logger logger = LoggerFactory.getLogger(LoyaltyService.class);
    private static final int POINTS_PER_DOLLAR = 1;
    private static final int POINTS_TO_DOLLAR_RATIO = 100; // 100 points = $1

    private final LoyaltyAccountRepository accountRepository;
    private final LoyaltyTransactionRepository transactionRepository;

    public LoyaltyService(LoyaltyAccountRepository accountRepository,
                          LoyaltyTransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public LoyaltyAccount getOrCreateAccount(String username) {
        return accountRepository.findByCustomerUsername(username)
                .orElseGet(() -> {
                    LoyaltyAccount newAccount = new LoyaltyAccount();
                    newAccount.setCustomerUsername(username);
                    return accountRepository.save(newAccount);
                });
    }

    public LoyaltyAccount getAccount(String username) {
        return accountRepository.findByCustomerUsername(username)
                .orElseThrow(() -> new RuntimeException("Loyalty account not found for user: " + username));
    }

    @Transactional
    public LoyaltyTransaction awardPoints(String username, Long orderId, BigDecimal orderAmount) {
        LoyaltyAccount account = getOrCreateAccount(username);
        
        // Calculate points: 1 point per $1 spent
        int pointsEarned = orderAmount.multiply(BigDecimal.valueOf(POINTS_PER_DOLLAR)).intValue();
        
        // Apply tier bonus
        double tierMultiplier = getTierMultiplier(account.getTier());
        pointsEarned = (int) (pointsEarned * tierMultiplier);
        
        account.addPoints(pointsEarned);
        accountRepository.save(account);
        
        // Create transaction record
        LoyaltyTransaction transaction = new LoyaltyTransaction();
        transaction.setAccount(account);
        transaction.setOrderId(orderId);
        transaction.setPoints(pointsEarned);
        transaction.setTransactionType(LoyaltyTransaction.TransactionType.EARNED);
        transaction.setDescription(String.format("Earned %d points for order #%d (%.1fx tier bonus)", 
                pointsEarned, orderId, tierMultiplier));
        
        LoyaltyTransaction saved = transactionRepository.save(transaction);
        
        logger.info("Awarded {} points to user {} for order {}. New balance: {}, Tier: {}", 
                pointsEarned, username, orderId, account.getPointsBalance(), account.getTier());
        
        return saved;
    }

    @Transactional
    public LoyaltyTransaction redeemPoints(String username, int points, String description) {
        LoyaltyAccount account = getAccount(username);
        
        if (points > account.getPointsBalance()) {
            throw new RuntimeException("Insufficient points. Available: " + account.getPointsBalance());
        }
        
        account.redeemPoints(points);
        accountRepository.save(account);
        
        // Create transaction record
        LoyaltyTransaction transaction = new LoyaltyTransaction();
        transaction.setAccount(account);
        transaction.setPoints(-points);
        transaction.setTransactionType(LoyaltyTransaction.TransactionType.REDEEMED);
        transaction.setDescription(description != null ? description : "Points redeemed");
        
        LoyaltyTransaction saved = transactionRepository.save(transaction);
        
        logger.info("Redeemed {} points for user {}. New balance: {}", 
                points, username, account.getPointsBalance());
        
        return saved;
    }

    public List<LoyaltyTransaction> getTransactionHistory(String username) {
        LoyaltyAccount account = getAccount(username);
        return transactionRepository.findByAccountIdOrderByCreatedAtDesc(account.getId());
    }

    public BigDecimal getRedeemableValue(String username) {
        LoyaltyAccount account = getAccount(username);
        return BigDecimal.valueOf(account.getPointsBalance())
                .divide(BigDecimal.valueOf(POINTS_TO_DOLLAR_RATIO));
    }

    private double getTierMultiplier(LoyaltyAccount.LoyaltyTier tier) {
        return switch (tier) {
            case BRONZE -> 1.0;
            case SILVER -> 1.25;
            case GOLD -> 1.5;
            case PLATINUM -> 2.0;
        };
    }
}
