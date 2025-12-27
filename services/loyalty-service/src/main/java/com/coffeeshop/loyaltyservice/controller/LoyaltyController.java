package com.coffeeshop.loyaltyservice.controller;

import com.coffeeshop.loyaltyservice.model.LoyaltyAccount;
import com.coffeeshop.loyaltyservice.model.LoyaltyTransaction;
import com.coffeeshop.loyaltyservice.service.LoyaltyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loyalty")
public class LoyaltyController {

    private final LoyaltyService loyaltyService;

    public LoyaltyController(LoyaltyService loyaltyService) {
        this.loyaltyService = loyaltyService;
    }

    @GetMapping("/account")
    public ResponseEntity<LoyaltyAccount> getAccount(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        LoyaltyAccount account = loyaltyService.getOrCreateAccount(username);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/balance")
    public ResponseEntity<Map<String, Object>> getBalance(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        LoyaltyAccount account = loyaltyService.getOrCreateAccount(username);
        BigDecimal redeemableValue = loyaltyService.getRedeemableValue(username);
        
        return ResponseEntity.ok(Map.of(
                "pointsBalance", account.getPointsBalance(),
                "tier", account.getTier().name(),
                "lifetimePoints", account.getLifetimePoints(),
                "redeemableValue", redeemableValue
        ));
    }

    @GetMapping("/history")
    public ResponseEntity<List<LoyaltyTransaction>> getTransactionHistory(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        List<LoyaltyTransaction> history = loyaltyService.getTransactionHistory(username);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/redeem")
    public ResponseEntity<LoyaltyTransaction> redeemPoints(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody RedeemRequest request) {
        String username = jwt.getClaimAsString("preferred_username");
        LoyaltyTransaction transaction = loyaltyService.redeemPoints(
                username, 
                request.points(), 
                request.description()
        );
        return ResponseEntity.ok(transaction);
    }

    public record RedeemRequest(int points, String description) {}
}
