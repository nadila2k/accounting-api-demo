package com.nadila.accounting_app_demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @PostMapping
    @PreAuthorize("hasAuthority('payment:create') or hasRole('SUPER_ADMIN')")
    public String createPayment() {
        return "Payment created successfully";
    }

    @GetMapping
    @PreAuthorize("hasAuthority('payment:read') or hasRole('SUPER_ADMIN')")
    public String getAllPayments() {
        return "List of all payments";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('payment:read') or hasRole('SUPER_ADMIN')")
    public String getPaymentById(@PathVariable Long id) {
        return "Payment details for ID: " + id;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('payment:update') or hasRole('SUPER_ADMIN')")
    public String updatePayment(@PathVariable Long id) {
        return "Payment updated with ID: " + id;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('payment:delete') or hasRole('SUPER_ADMIN')")
    public String deletePayment(@PathVariable Long id) {
        return "Payment deleted with ID: " + id;
    }
}