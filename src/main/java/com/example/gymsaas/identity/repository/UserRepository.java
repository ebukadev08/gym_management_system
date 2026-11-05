package com.example.gymsaas.identity.repository;

import com.example.gymsaas.identity.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByExternalIdentityId(String externalIdentityId);
    boolean existsByTenantIdAndEmailIgnoreCase(UUID tenantId, String email);
    List<User> findByTenantId(UUID tenantId);
    /** Loads the user with roles and permissions in one query (used by Spring Security). */
    @Query("select distinct u from User u left join fetch u.roles r left join fetch r.permissions "
            + "where u.externalIdentityId = :externalId")
    Optional<User> findWithRolesByExternalIdentityId(@Param("externalId") String externalId);
}
