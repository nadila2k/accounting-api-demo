package com.nadila.accounting_app_demo.enums;

public enum AppPermission {

    // ── Audit ─────────────────────────────────────────────────────────────────
    AUDIT_READ   ("audit:read",    "View audit records"),
    AUDIT_CREATE ("audit:create",  "Create audit records"),
    AUDIT_UPDATE ("audit:update",  "Edit audit records"),
    AUDIT_DELETE ("audit:delete",  "Delete audit records"),

    // ── Finance – write ───────────────────────────────────────────────────────
    FINANCE_CREATE ("finance:create", "Create finance records"),
    FINANCE_UPDATE ("finance:update", "Edit finance records"),
    FINANCE_DELETE ("finance:delete", "Delete finance records"),

    // ── Finance – granular read  (one permission per GET endpoint) ────────────
    FINANCE_READ         ("finance:read",         "List all finance records"),
    FINANCE_READ_BY_ID   ("finance:read:id",      "Get finance record by ID"),
    FINANCE_READ_BY_DATE ("finance:read:date",     "Get finance records by date"),
    FINANCE_READ_BY_TYPE ("finance:read:type",     "Get finance records by type"),
    FINANCE_READ_SUMMARY ("finance:read:summary",  "Get finance summary report"),

    // ── Payment ───────────────────────────────────────────────────────────────
    PAYMENT_READ   ("payment:read",    "View payments"),
    PAYMENT_CREATE ("payment:create",  "Create payments"),
    PAYMENT_UPDATE ("payment:update",  "Edit payments"),
    PAYMENT_DELETE ("payment:delete",  "Delete payments"),

    // ── Product ───────────────────────────────────────────────────────────────
    PRODUCT_READ   ("product:read",    "View products"),
    PRODUCT_CREATE ("product:create",  "Create products"),
    PRODUCT_UPDATE ("product:update",  "Edit products"),
    PRODUCT_DELETE ("product:delete",  "Delete products");

    private final String key;
    private final String description;

    AppPermission(String key, String description) {
        this.key         = key;
        this.description = description;
    }

    /** The string stored in DB and used in @PreAuthorize expressions. */
    public String key()         { return key; }
    public String description() { return description; }

    @Override
    public String toString()    { return key; }
}
