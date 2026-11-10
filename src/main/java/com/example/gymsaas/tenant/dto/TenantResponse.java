package com.example.gymsaas.tenant.dto;

import java.util.UUID;

public record TenantResponse(
        UUID tenantId,
        String gymName,
        String slug,
        UUID adminUserId,
        String adminEmail
) {
}