package com.example.gymsaas.common.tenant;

import java.util.UUID;
import java.util.function.Supplier;

public final class TenantContext {

    /** Used when no tenant is set. It matches no real tenant, so nothing is returned. */
    public static final UUID NO_TENANT = new UUID(0L, 0L);

    private static final ThreadLocal<UUID> CURRENT = new ThreadLocal<>();

    private TenantContext() {
    }

    public static void setTenantId(UUID tenantId) {
        CURRENT.set(tenantId);
    }

    public static UUID getTenantId() {
        return CURRENT.get();
    }

    public static void clear() {
        CURRENT.remove();
    }

    /** Runs an action as a given tenant, then restores the previous one. */
    public static <T> T callAs(UUID tenantId, Supplier<T> action) {
        UUID previous = CURRENT.get();
        try {
            CURRENT.set(tenantId);
            return action.get();
        } finally {
            if (previous == null) {
                CURRENT.remove();
            } else {
                CURRENT.set(previous);
            }
        }
    }
}