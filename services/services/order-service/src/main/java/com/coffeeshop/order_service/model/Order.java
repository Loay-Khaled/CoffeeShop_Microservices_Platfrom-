package com.coffeeshop.orderservice.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="customer_username", nullable=false, length=100)
    private String customerUsername;

    @Column(nullable=false, length=30)
    private String status;

    @Column(nullable=false, precision=10, scale=2)
    private BigDecimal total;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy="order", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = "CREATED";
        if (total == null) total = BigDecimal.ZERO;
    }

    // getters/setters
    public Long getId(){return id;}
    public String getCustomerUsername(){return customerUsername;}
    public void setCustomerUsername(String v){customerUsername=v;}
    public String getStatus(){return status;}
    public void setStatus(String v){status=v;}
    public BigDecimal getTotal(){return total;}
    public void setTotal(BigDecimal v){total=v;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public List<OrderItem> getItems(){return items;}
    public void setItems(List<OrderItem> items){this.items=items;}
}