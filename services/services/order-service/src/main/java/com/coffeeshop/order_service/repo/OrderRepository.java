package com.coffeeshop.orderservice.repo;

import com.coffeeshop.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}