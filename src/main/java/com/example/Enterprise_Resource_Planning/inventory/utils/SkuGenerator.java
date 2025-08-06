package com.example.Enterprise_Resource_Planning.inventory.utils;

import org.springframework.stereotype.Component;

import com.example.Enterprise_Resource_Planning.inventory.exception.InvalidSkuException;

@Component
public class SkuGenerator {

    public String generate(String name, Long id) {
        if (name == null || name.length() < 3 || id == null) {
            throw new InvalidSkuException("Invalid name or ID for SKU generation");
        }

        String prefix = name.replaceAll("\\s+", "")
                            .substring(0, Math.min(3, name.length()))
                            .toUpperCase();

        return prefix + "-" + id;
    }
}