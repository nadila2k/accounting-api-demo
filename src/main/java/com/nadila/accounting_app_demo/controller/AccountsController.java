package com.nadila.accounting_app_demo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountsController {

    @PostMapping
    public String createAccount() {
        return "Account created successfully";
    }

    @GetMapping
    public String getAllAccounts() {
        return "List of all accounts";
    }

    @GetMapping("/{id}")
    public String getAccountById(@PathVariable Long id) {
        return "Account details for ID: " + id;
    }

    @PutMapping("/{id}")
    public String updateAccount(@PathVariable Long id) {
        return "Account updated with ID: " + id;
    }

    @DeleteMapping("/{id}")
    public String deleteAccount(@PathVariable Long id) {
        return "Account deleted with ID: " + id;
    }
}