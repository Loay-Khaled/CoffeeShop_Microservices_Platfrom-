package com.coffeeshop.catalogservice.repo;

import com.coffeeshop.catalogservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}