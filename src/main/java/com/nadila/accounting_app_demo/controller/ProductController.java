package com.nadila.accounting_app_demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @PostMapping
    @PreAuthorize("hasAuthority('product:create') or hasRole('SUPER_ADMIN')")
    public String createProduct() {
        return "Product created successfully";
    }

    @GetMapping
    @PreAuthorize("hasAuthority('product:read') or hasRole('SUPER_ADMIN')")
    public String getAllProducts() {
        return "List of all products";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('product:read') or hasRole('SUPER_ADMIN')")
    public String getProductById(@PathVariable Long id) {
        return "Product details for ID: " + id;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('product:update') or hasRole('SUPER_ADMIN')")
    public String updateProduct(@PathVariable Long id) {
        return "Product updated with ID: " + id;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('product:delete') or hasRole('SUPER_ADMIN')")
    public String deleteProduct(@PathVariable Long id) {
        return "Product deleted with ID: " + id;
    }
}