package com.coffeeshop.catalogservice.controller;

import com.coffeeshop.catalogservice.dto.CreateProductRequest;
import com.coffeeshop.catalogservice.model.Product;
import com.coffeeshop.catalogservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final ProductService service;

    public CatalogController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/items")
    public List<Product> list() {
        return service.findAll();
    }

    @GetMapping("/items/{id}")
    public Product getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody CreateProductRequest req) {
        return service.create(req);
    }

    @PutMapping("/items/{id}")
    public Product update(@PathVariable Long id, @RequestBody CreateProductRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}