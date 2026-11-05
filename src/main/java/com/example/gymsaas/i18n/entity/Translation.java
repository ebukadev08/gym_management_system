package com.example.gymsaas.i18n.entity;

import com.example.gymsaas.common.entity.TenantAwareEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "translations")
public class Translation extends TenantAwareEntity {

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @Column(name = "field_name", nullable = false)
    private String fieldName;

    @Column(name = "language_code", nullable = false, length = 5)
    private String languageCode;

    @Column(name = "value", nullable = false, columnDefinition = "text")
    private String value;
}
