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
public interface TrainerAssignmentRepository extends JpaRepository<TrainerAssignment, UUID> {

    Optional<TrainerAssignment> findByTrainerIdAndClassSessionIdAndUnassignedAtIsNull(UUID trainerId, UUID classSessionId);
    List<TrainerAssignment> findByClassSessionIdAndUnassignedAtIsNull(UUID classSessionId);
}
