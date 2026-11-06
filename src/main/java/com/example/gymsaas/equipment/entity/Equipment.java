package com.example.gymsaas.equipment.entity;

import com.example.gymsaas.common.entity.TenantAwareEntity;
import com.example.gymsaas.equipment.enums.EquipmentStatus;

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
@Table(name = "equipment")
public class Equipment extends TenantAwareEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "category_id", nullable = true)
    private EquipmentCategory category;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "serial_number")
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EquipmentStatus status = EquipmentStatus.AVAILABLE;

    @Column(name = "purchased_at")
    private LocalDate purchasedAt;
}
