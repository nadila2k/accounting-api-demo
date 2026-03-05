package com.nadila.accounting_app_demo.controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/audit")
public class AuditController {

    @PostMapping
    @PreAuthorize("hasAuthority('audit:create') or hasRole('SUPER_ADMIN')")
    public String createAudit() {
        return "Audit record created";
    }

    @GetMapping
    @PreAuthorize("hasAuthority('audit:read') or hasRole('SUPER_ADMIN')")
    public String getAllAudits() {
        return "List of all audit records";
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('audit:update') or hasRole('SUPER_ADMIN')")
    public String updateAudit(@PathVariable Long id) {
        return "Audit updated with ID: " + id;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('audit:delete') or hasRole('SUPER_ADMIN')")
    public String deleteAudit(@PathVariable Long id) {
        return "Audit deleted with ID: " + id;
    }
}