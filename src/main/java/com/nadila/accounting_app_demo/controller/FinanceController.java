package com.nadila.accounting_app_demo.controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/finance")
public class FinanceController {

    @PostMapping
    @PreAuthorize("hasAuthority('finance:create') or hasRole('SUPER_ADMIN')")
    public String createFinanceRecord() {
        return "Finance record created";
    }

    // ── READ: list all  →  finance:read ──────────────────────────────────────
    @GetMapping
    @PreAuthorize("hasAuthority('finance:read') or hasRole('SUPER_ADMIN')")
    public String getFinanceRecords() {
        return "List of finance records";
    }

    // ── READ: by ID  →  finance:read:id ──────────────────────────────────────
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('finance:read:id') or hasRole('SUPER_ADMIN')")
    public String getFinanceById(@PathVariable Long id) {
        return "Finance record details for ID: " + id;
    }

    // ── READ: by date  →  finance:read:date ──────────────────────────────────
    @GetMapping("/date/{date}")
    @PreAuthorize("hasAuthority('finance:read:date') or hasRole('SUPER_ADMIN')")
    public String getFinanceByDate(@PathVariable String date) {
        return "Finance records for date: " + date;
    }

    // ── READ: by type  →  finance:read:type ──────────────────────────────────
    @GetMapping("/type/{type}")
    @PreAuthorize("hasAuthority('finance:read:type') or hasRole('SUPER_ADMIN')")
    public String getFinanceByType(@PathVariable String type) {
        return "Finance records for type: " + type;
    }

    // ── READ: summary  →  finance:read:summary ────────────────────────────────
    @GetMapping("/summary")
    @PreAuthorize("hasAuthority('finance:read:summary') or hasRole('SUPER_ADMIN')")
    public String getFinanceSummary() {
        return "Finance summary report";
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('finance:update') or hasRole('SUPER_ADMIN')")
    public String updateFinanceRecord(@PathVariable Long id) {
        return "Finance record updated with ID: " + id;
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('finance:delete') or hasRole('SUPER_ADMIN')")
    public String deleteFinanceRecord(@PathVariable Long id) {
        return "Finance record deleted with ID: " + id;
    }
}
