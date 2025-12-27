package com.coffeeshop.loyaltyservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loyalty_accounts")
public class LoyaltyAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_username", nullable = false, unique = true)
    private String customerUsername;

    @Column(name = "points_balance")
    private int pointsBalance = 0;

    @Column(name = "tier")
    @Enumerated(EnumType.STRING)
    private LoyaltyTier tier = LoyaltyTier.BRONZE;

    @Column(name = "lifetime_points")
    private int lifetimePoints = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum LoyaltyTier {
        BRONZE,  // 0-499 lifetime points
        SILVER,  // 500-1499 lifetime points
        GOLD,    // 1500-2999 lifetime points
        PLATINUM // 3000+ lifetime points
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerUsername() { return customerUsername; }
    public void setCustomerUsername(String customerUsername) { this.customerUsername = customerUsername; }

    public int getPointsBalance() { return pointsBalance; }
    public void setPointsBalance(int pointsBalance) { this.pointsBalance = pointsBalance; }

    public LoyaltyTier getTier() { return tier; }
    public void setTier(LoyaltyTier tier) { this.tier = tier; }

    public int getLifetimePoints() { return lifetimePoints; }
    public void setLifetimePoints(int lifetimePoints) { this.lifetimePoints = lifetimePoints; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public void addPoints(int points) {
        this.pointsBalance += points;
        this.lifetimePoints += points;
        this.updatedAt = LocalDateTime.now();
        updateTier();
    }

    public void redeemPoints(int points) {
        if (points > this.pointsBalance) {
            throw new RuntimeException("Insufficient points balance");
        }
        this.pointsBalance -= points;
        this.updatedAt = LocalDateTime.now();
    }

    private void updateTier() {
        if (lifetimePoints >= 3000) {
            this.tier = LoyaltyTier.PLATINUM;
        } else if (lifetimePoints >= 1500) {
            this.tier = LoyaltyTier.GOLD;
        } else if (lifetimePoints >= 500) {
            this.tier = LoyaltyTier.SILVER;
        } else {
            this.tier = LoyaltyTier.BRONZE;
        }
    }
}
