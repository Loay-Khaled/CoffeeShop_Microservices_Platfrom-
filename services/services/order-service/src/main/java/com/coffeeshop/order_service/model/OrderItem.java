package com.coffeeshop.orderservice.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="order_id")
    private Order order;

    @Column(name="product_id", nullable=false)
    private Long productId;

    @Column(name="product_name", nullable=false, length=200)
    private String productName;

    @Column(name="unit_price", nullable=false, precision=10, scale=2)
    private BigDecimal unitPrice;

    @Column(nullable=false)
    private Integer quantity;

    // getters/setters
    public Long getId(){return id;}
    public Order getOrder(){return order;}
    public void setOrder(Order o){order=o;}
    public Long getProductId(){return productId;}
    public void setProductId(Long v){productId=v;}
    public String getProductName(){return productName;}
    public void setProductName(String v){productName=v;}
    public BigDecimal getUnitPrice(){return unitPrice;}
    public void setUnitPrice(BigDecimal v){unitPrice=v;}
    public Integer getQuantity(){return quantity;}
    public void setQuantity(Integer v){quantity=v;}
}