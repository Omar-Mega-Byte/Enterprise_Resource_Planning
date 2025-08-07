package com.example.Enterprise_Resource_Planning.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Simple Security Configuration for Inventory Module
 * Provides basic authentication and filtration for inventory controllers
 */
@Configuration
@EnableWebSecurity
public class InventorySecurityConfig {

    /**
     * Password encoder for inventory module
     */
    @Bean
    public PasswordEncoder inventoryPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Security filter chain specifically for inventory endpoints
     */
    @Bean
    @Order(2)
    public SecurityFilterChain inventorySecurityFilterChain(HttpSecurity http) throws Exception {
        http
            // Apply security only to inventory endpoints
            .securityMatcher("/inventory/**")
            
            // Disable CSRF for REST API
            .csrf(csrf -> csrf.disable())
            
            // Stateless session management
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Simple filtration rules for inventory controllers
            .authorizeHttpRequests(auth -> auth
                // Product Controller Filtrations
                .requestMatchers(HttpMethod.GET, "/inventory/products/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/inventory/products").permitAll()
                .requestMatchers(HttpMethod.PUT, "/inventory/products/**").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/inventory/products/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/inventory/products/**").permitAll()
                
                // Dashboard Controller Filtrations
                .requestMatchers("/inventory/dashboard/**").permitAll()
                
                // Search operations - allow authenticated users
                .requestMatchers("/inventory/products/search/**").permitAll()
                .requestMatchers("/inventory/products/reorder-alerts").permitAll()
                .requestMatchers("/inventory/products/low-stock").permitAll()
                
                // Utility operations
                .requestMatchers("/inventory/products/check-sku").permitAll()
                
                // Default - require authentication for all other inventory endpoints
                .anyRequest().authenticated()
            )
            
            // Basic authentication
            .httpBasic(httpBasic -> httpBasic
                .realmName("Inventory Module")
            );

        return http.build();
    }
}
