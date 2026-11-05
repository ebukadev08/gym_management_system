package com.example.gymsaas.gymClasses.repository;

import com.example.gymsaas.gymClasses.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClassRegistrationRepository extends JpaRepository<ClassRegistration, UUID> {

    boolean existsByMemberIdAndClassSessionId(UUID memberId, UUID classSessionId);
    long countByClassSessionIdAndStatus(UUID classSessionId, RegistrationStatus status);
    List<ClassRegistration> findByMemberId(UUID memberId);
}
