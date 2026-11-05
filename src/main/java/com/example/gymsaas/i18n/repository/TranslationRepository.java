package com.example.gymsaas.i18n.repository;

import com.example.gymsaas.i18n.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, UUID> {

    List<Translation> findByEntityTypeAndEntityId(String entityType, UUID entityId);
    Optional<Translation> findByEntityTypeAndEntityIdAndFieldNameAndLanguageCode(String entityType, UUID entityId, String fieldName, String languageCode);
}
