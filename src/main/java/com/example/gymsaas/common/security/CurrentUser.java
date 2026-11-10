package com.example.gymsaas.common.security;

import java.util.UUID;

public record CurrentUser(UUID userId, UUID tenantId, String email) {
}