package com.coffeeshop.orderservice.controller;

import com.coffeeshop.orderservice.dto.AddItemRequest;
import com.coffeeshop.orderservice.dto.ProductDto;
import com.coffeeshop.orderservice.event.OrderCreatedEvent;
import com.coffeeshop.orderservice.model.Order;
import com.coffeeshop.orderservice.model.OrderItem;
import com.coffeeshop.orderservice.repo.OrderItemRepository;
import com.coffeeshop.orderservice.repo.OrderRepository;
import com.coffeeshop.orderservice.service.OrderEventService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepo;
    private final OrderItemRepository itemRepo;
    private final WebClient webClient;   // ✅ 1) لازم يتضاف

    // ✅ 2) لازم webClient يدخل في الـ constructor
    public OrderController(OrderRepository orderRepo, OrderItemRepository itemRepo, WebClient webClient) {
        this.orderRepo = orderRepo;
        this.itemRepo = itemRepo;
        this.webClient = webClient;
    }

    // ✅ Create Order
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order create(@RequestParam String customer) {
        Order o = new Order();
        o.setCustomerUsername(customer);
        o.setStatus("CREATED");
        o.setTotal(BigDecimal.ZERO);
        return orderRepo.save(o);
    }

    // ✅ Get Order by id
    @GetMapping("/{id}")
    public Order get(@PathVariable Long id) {
        return orderRepo.findById(id).orElseThrow();
    }

    // ✅ Get my orders using username from JWT token
    @GetMapping("/me")
    public List<Order> myOrders(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        return orderRepo.findByCustomerUsername(username);
    }

    // ✅ Add item to order (fetch product from catalog-service)
    @PostMapping("/{id}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public Order addItem(
            @PathVariable Long id,
            @RequestBody AddItemRequest req,
            @RequestHeader("Authorization") String authHeader
    ) {
        Order order = orderRepo.findById(id).orElseThrow();

        String token = authHeader.replace("Bearer ", "");

        ProductDto product = webClient.get()
                .uri("http://localhost:8082/api/catalog/items/{pid}", req.getProductId())
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProductId(product.id);
        item.setProductName(product.name);
        item.setUnitPrice(product.price);
        item.setQuantity(req.getQuantity());

        itemRepo.save(item);

        BigDecimal lineTotal = product.price.multiply(BigDecimal.valueOf(req.getQuantity()));
        order.setTotal(order.getTotal().add(lineTotal));

        return orderRepo.save(order);
    }

    // ✅ Update order status
    @PutMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id, @RequestParam String status) {
        Order order = orderRepo.findById(id).orElseThrow();
        order.setStatus(status);
        return orderRepo.save(order);
    }

    // ✅ Get ALL orders (Admin only)
    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    // ✅ Delete order (Admin only)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long id) {
        Order order = orderRepo.findById(id).orElseThrow();
        // Delete all order items first
        itemRepo.deleteAll(order.getItems());
        orderRepo.delete(order);
    }

    // ✅ Cancel order (Customer can cancel their own order)
    @PutMapping("/{id}/cancel")
    public Order cancelOrder(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        Order order = orderRepo.findById(id).orElseThrow();
        String username = jwt.getClaimAsString("preferred_username");
        
        // Check if user owns this order or is admin
        boolean isAdmin = username.equals("admin1");
        if (!order.getCustomerUsername().equals(username) && !isAdmin) {
            throw new RuntimeException("You can only cancel your own orders");
        }
        
        // Only allow cancellation if order is not already completed or cancelled
        if (order.getStatus().equals("COMPLETED") || order.getStatus().equals("CANCELLED")) {
            throw new RuntimeException("Cannot cancel order with status: " + order.getStatus());
        }
        
        order.setStatus("CANCELLED");
        return orderRepo.save(order);
    }
}