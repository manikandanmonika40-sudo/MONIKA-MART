package com.monikamart.monikamart.config;

import com.monikamart.monikamart.entity.Category;
import com.monikamart.monikamart.entity.Product;
import com.monikamart.monikamart.entity.User;
import com.monikamart.monikamart.repository.CategoryRepository;
import com.monikamart.monikamart.repository.ProductRepository;
import com.monikamart.monikamart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public DataInitializer(UserRepository userRepository, CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedCategories();
        seedProducts();
    }

    private void seedUsers() {
        // 1. Seed Admin user
        if (!userRepository.existsByEmail("admin@monikamart.com")) {
            User admin = new User(
                    "MONIKA MART Admin",
                    "admin@monikamart.com",
                    "+91 9876543210",
                    "admin123",
                    "Admin Headquarters, Tech Park",
                    "Bengaluru",
                    "Karnataka",
                    "560001",
                    "ADMIN"
            );
            userRepository.save(admin);
            System.out.println("✅ Seeded Admin Account: admin@monikamart.com / admin123");
        }

        // 2. Seed Demo Customer user
        if (!userRepository.existsByEmail("monika@example.com")) {
            User customer = new User(
                    "Monika Sharma",
                    "monika@example.com",
                    "+91 9812345678",
                    "monika123",
                    "42 Orchid Avenue, Blossom Hill",
                    "Mumbai",
                    "Maharashtra",
                    "400050",
                    "USER"
            );
            userRepository.save(customer);
            System.out.println("✅ Seeded Demo Customer: monika@example.com / monika123");
        }
    }

    private void seedCategories() {
        List<Category> defaultCategories = Arrays.asList(
                new Category("Electronics", "Smart gadgets, cutting-edge laptops, audio gear & displays", "fa-laptop", "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&q=80"),
                new Category("Fashion", "Trending apparel, premium denim, and streetwear", "fa-shirt", "https://images.unsplash.com/photo-1489987707025-afc232f7ea0f?w=600&q=80"),
                new Category("Home & Kitchen", "Modern kitchenware, ergonomic seating & home comfort", "fa-couch", "https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=600&q=80"),
                new Category("Accessories", "Designer backpacks, stylish watches, and tech accessories", "fa-gem", "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&q=80"),
                new Category("Beauty", "Skincare essentials, fragrances, and grooming products", "fa-spa", "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=600&q=80"),
                new Category("Sports", "Performance sports footwear, gear & hydration bottles", "fa-basketball", "https://images.unsplash.com/photo-1517649763962-0c623266ddc0?w=600&q=80"),
                new Category("Stationery", "Artisan notebooks, signature pens & creative desk essentials", "fa-pen-ruler", "https://images.unsplash.com/photo-1583485088034-697b5bc54ccd?w=600&q=80")
        );

        for (Category cat : defaultCategories) {
            if (!categoryRepository.existsByName(cat.getName())) {
                categoryRepository.save(cat);
            }
        }
        System.out.println("✅ Seeded default categories");
    }

    private void seedProducts() {
        if (productRepository.count() > 0) {
            return; // Products already present
        }

        List<Product> products = Arrays.asList(
                // Electronics
                new Product(
                        "Apple MacBook Pro M3 (14-inch)",
                        "Supercharged by M3 chip with 8-core CPU and 10-core GPU. Stunning Liquid Retina XDR display with up to 22 hours of battery life.",
                        1499.00,
                        "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=800&q=80",
                        "Electronics",
                        25,
                        4.9,
                        142,
                        true,
                        true
                ),
                new Product(
                        "Samsung Galaxy S24 Ultra (512GB)",
                        "Titanium frame, 200MP AI camera system with 100x Space Zoom, Snapdragon 8 Gen 3, and integrated S-Pen stylus.",
                        1199.00,
                        "https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=800&q=80",
                        "Electronics",
                        30,
                        4.8,
                        98,
                        true,
                        true
                ),
                new Product(
                        "Sony WH-1000XM5 Wireless Headphones",
                        "Industry-leading noise canceling with two processors and 8 microphones. Ultra-comfortable lightweight design and 30-hour battery life.",
                        399.00,
                        "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&q=80",
                        "Electronics",
                        45,
                        4.9,
                        310,
                        true,
                        true
                ),
                new Product(
                        "Apple Watch Series 9 GPS 45mm",
                        "S9 chip powers a super-bright display, double tap gesture, advanced health sensors with ECG, and Crash Detection.",
                        429.00,
                        "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800&q=80",
                        "Electronics",
                        50,
                        4.7,
                        185,
                        true,
                        false
                ),
                new Product(
                        "Logitech MX Master 3S Wireless Mouse",
                        "Quiet Clicks, 8K DPI any-surface tracking, ergonomic silhouette, and MagSpeed electromagnetic scrolling.",
                        99.00,
                        "https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=800&q=80",
                        "Electronics",
                        60,
                        4.8,
                        220,
                        false,
                        true
                ),
                new Product(
                        "Keychron K2 Mechanical Keyboard",
                        "Compact 75% layout wireless mechanical keyboard with RGB backlighting, Mac/Windows layout switch, and hot-swappable switches.",
                        89.00,
                        "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=800&q=80",
                        "Electronics",
                        35,
                        4.6,
                        94,
                        false,
                        true
                ),
                new Product(
                        "LG UltraFine 27-inch 4K UHD Monitor",
                        "IPS display with HDR10, sRGB 98% color gamut, AMD FreeSync, and virtually borderless ergonomic design.",
                        349.00,
                        "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=800&q=80",
                        "Electronics",
                        20,
                        4.7,
                        67,
                        true,
                        false
                ),
                new Product(
                        "Anker 24,000mAh Power Bank (140W)",
                        "Ultra-powerful 3-port portable charger with smart digital display and lightning-fast Power Delivery 3.1 charging.",
                        149.00,
                        "https://images.unsplash.com/photo-1609592424368-232f305fcf5a?w=800&q=80",
                        "Electronics",
                        80,
                        4.8,
                        150,
                        false,
                        false
                ),
                new Product(
                        "JBL Flip 6 Portable Bluetooth Speaker",
                        "Eco-friendly waterproof and dustproof speaker with 2-way speaker system delivering booming bass and crystal clear highs.",
                        129.00,
                        "https://images.unsplash.com/photo-1545454675-3531b543be5d?w=800&q=80",
                        "Electronics",
                        65,
                        4.7,
                        180,
                        false,
                        true
                ),
                new Product(
                        "Canon PIXMA Wireless Color Printer",
                        "All-in-one wireless color inkjet printer with auto 2-sided printing, mobile print app, and crisp photo reproduction.",
                        179.00,
                        "https://images.unsplash.com/photo-1612815154858-60aa4c59eaa6?w=800&q=80",
                        "Electronics",
                        15,
                        4.4,
                        42,
                        false,
                        false
                ),

                // Fashion
                new Product(
                        "Urban Classic Heavyweight Cotton T-Shirt",
                        "Crafted from 100% combed organic ring-spun cotton. Pre-shrunk, breathable, with reinforced double stitching.",
                        29.00,
                        "https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=800&q=80",
                        "Fashion",
                        120,
                        4.6,
                        89,
                        false,
                        true
                ),
                new Product(
                        "Levi's 511 Slim Fit Stretch Denim Jeans",
                        "Modern slim-cut jeans with room to move. Premium durable denim with stretch technology for all-day comfort.",
                        69.00,
                        "https://images.unsplash.com/photo-1542272604-780c96856592?w=800&q=80",
                        "Fashion",
                        85,
                        4.7,
                        210,
                        true,
                        false
                ),

                // Sports
                new Product(
                        "Nike Air Zoom Pegasus Running Shoes",
                        "Engineered mesh upper for breathability, responsive React foam cushioning, and dual Zoom Air units for a springy stride.",
                        130.00,
                        "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=800&q=80",
                        "Sports",
                        50,
                        4.9,
                        320,
                        true,
                        true
                ),
                new Product(
                        "Hydro Flask 32 oz Wide Mouth Insulated Bottle",
                        "TempShield double-wall vacuum insulation keeps drinks cold up to 24 hours or piping hot up to 12 hours. Pro-grade stainless steel.",
                        44.00,
                        "https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=800&q=80",
                        "Sports",
                        95,
                        4.8,
                        175,
                        false,
                        true
                ),

                // Accessories
                new Product(
                        "Nordic Leather Daypack & Laptop Backpack",
                        "Water-resistant vintage aesthetic with padded 16-inch laptop compartment, ergonomic straps, and hidden anti-theft pocket.",
                        89.00,
                        "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=800&q=80",
                        "Accessories",
                        40,
                        4.7,
                        115,
                        true,
                        false
                ),

                // Home & Kitchen
                new Product(
                        "Ergonomic High-Back Executive Office Chair",
                        "Breathable mesh back with dynamic lumbar support, 3D adjustable armrests, and 135-degree tilt reclining lock.",
                        249.00,
                        "https://images.unsplash.com/photo-1580481077194-48f8605c4856?w=800&q=80",
                        "Home & Kitchen",
                        25,
                        4.8,
                        88,
                        true,
                        false
                ),
                new Product(
                        "Samsung 55-inch Crystal 4K Smart TV",
                        "Dynamic Crystal Color, Crystal Processor 4K, Object Tracking Sound Lite, and slim AirSlim minimalist bezel design.",
                        599.00,
                        "https://images.unsplash.com/photo-1593359677879-a4bb92f829d1?w=800&q=80",
                        "Home & Kitchen",
                        18,
                        4.7,
                        140,
                        true,
                        true
                ),

                // Stationery
                new Product(
                        "Moleskine Classic Hardcover Dotted Notebook",
                        "Reliable travel companion with acid-free ivory pages, elastic closure band, matching ribbon bookmark, and expandable inner pocket.",
                        22.00,
                        "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=800&q=80",
                        "Stationery",
                        150,
                        4.9,
                        310,
                        false,
                        false
                ),
                new Product(
                        "Parker Sonnet Luxury Matte Black Fountain Pen",
                        "Hand-assembled with 18k solid gold finish nib. Exceptional precision and fluid writing experience.",
                        110.00,
                        "https://images.unsplash.com/photo-1583485088034-697b5bc54ccd?w=800&q=80",
                        "Stationery",
                        40,
                        4.8,
                        76,
                        false,
                        false
                ),

                // Electronics / Tablet
                new Product(
                        "Apple iPad Air M2 (11-inch 128GB)",
                        "Incredible M2 performance, Liquid Retina display, support for Apple Pencil Pro and Magic Keyboard, all-day battery life.",
                        599.00,
                        "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=800&q=80",
                        "Electronics",
                        35,
                        4.9,
                        195,
                        true,
                        true
                )
        );

        productRepository.saveAll(products);
        System.out.println("✅ Seeded 20 realistic sample products across categories in MySQL");
    }
}
