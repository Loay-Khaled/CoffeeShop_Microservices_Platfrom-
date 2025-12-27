package com.coffeeshop.orderservice.controller;

import com.coffeeshop.orderservice.model.Order;
import com.coffeeshop.orderservice.repo.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository repo;

    public OrderController(OrderRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order create(@RequestParam String customer) {
        Order o = new Order();
        o.setCustomerUsername(customer);
        o.setTotal(BigDecimal.ZERO);
        return repo.save(o);
    }

    @GetMapping("/{id}")
    public Order get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }
}