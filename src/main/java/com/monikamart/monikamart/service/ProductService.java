package com.monikamart.monikamart.service;

import com.monikamart.monikamart.entity.Product;
import com.monikamart.monikamart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts(String keyword, String category, Double minPrice, Double maxPrice, String sortBy) {
        List<Product> products = productRepository.filterProducts(
                keyword != null && !keyword.isBlank() ? keyword.trim() : null,
                category != null && !category.isBlank() ? category.trim() : null,
                minPrice,
                maxPrice
        );

        if (sortBy != null && !sortBy.isBlank()) {
            switch (sortBy.toLowerCase()) {
                case "pricelowtohigh":
                case "price_asc":
                case "price-asc":
                    products.sort(Comparator.comparing(Product::getPrice));
                    break;
                case "pricehightolow":
                case "price_desc":
                case "price-desc":
                    products.sort(Comparator.comparing(Product::getPrice).reversed());
                    break;
                case "customerrating":
                case "rating":
                case "rating_desc":
                    products.sort(Comparator.comparing(Product::getRating, Comparator.nullsLast(Comparator.reverseOrder())));
                    break;
                case "newest":
                case "newestarrivals":
                default:
                    products.sort(Comparator.comparing(Product::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())));
                    break;
            }
        }

        return products;
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }

    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return productRepository.findAll();
        }
        return productRepository.searchByKeyword(keyword.trim());
    }

    public List<Product> getFeaturedProducts() {
        return productRepository.findByIsFeaturedTrue();
    }

    public List<Product> getTrendingProducts() {
        return productRepository.findByIsTrendingTrue();
    }

    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setImageUrl(productDetails.getImageUrl());
        product.setCategory(productDetails.getCategory());
        product.setStock(productDetails.getStock());
        if (productDetails.getRating() != null) {
            product.setRating(productDetails.getRating());
        }
        if (productDetails.getReviewCount() != null) {
            product.setReviewCount(productDetails.getReviewCount());
        }
        if (productDetails.getIsFeatured() != null) {
            product.setIsFeatured(productDetails.getIsFeatured());
        }
        if (productDetails.getIsTrending() != null) {
            product.setIsTrending(productDetails.getIsTrending());
        }

        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    public long countProducts() {
        return productRepository.count();
    }
}
