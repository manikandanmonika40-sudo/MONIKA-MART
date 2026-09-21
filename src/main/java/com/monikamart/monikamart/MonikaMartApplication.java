package com.monikamart.monikamart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MonikaMartApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonikaMartApplication.class, args);
        System.out.println("==================================================");
        System.out.println("  🛍️  MONIKA MART Backend Server is Running!     ");
        System.out.println("  🌐  API Base URL: http://localhost:8080/api    ");
        System.out.println("==================================================");
    }
}
