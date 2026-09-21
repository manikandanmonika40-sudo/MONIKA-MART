package com.monikamart.monikamart.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column
    private Double rating = 4.5;

    @Column(name = "review_count")
    private Integer reviewCount = 0;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "is_trending")
    private Boolean isTrending = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Product() {
    }

    public Product(String name, String description, Double price, String imageUrl, String category, Integer stock, Double rating, Integer reviewCount) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.stock = stock != null ? stock : 0;
        this.rating = rating != null ? rating : 4.5;
        this.reviewCount = reviewCount != null ? reviewCount : 0;
    }

    public Product(String name, String description, Double price, String imageUrl, String category, Integer stock, Double rating, Integer reviewCount, Boolean isFeatured, Boolean isTrending) {
        this(name, description, price, imageUrl, category, stock, rating, reviewCount);
        this.isFeatured = isFeatured != null ? isFeatured : false;
        this.isTrending = isTrending != null ? isTrending : false;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.stock == null) {
            this.stock = 0;
        }
        if (this.rating == null) {
            this.rating = 4.5;
        }
        if (this.reviewCount == null) {
            this.reviewCount = 0;
        }
        if (this.isFeatured == null) {
            this.isFeatured = false;
        }
        if (this.isTrending == null) {
            this.isTrending = false;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Boolean getIsTrending() {
        return isTrending;
    }

    public void setIsTrending(Boolean isTrending) {
        this.isTrending = isTrending;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
