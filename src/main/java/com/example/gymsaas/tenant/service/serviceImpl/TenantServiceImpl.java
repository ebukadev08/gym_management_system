package com.example.gymsaas.tenant.service.serviceImpl;

import com.example.gymsaas.billing.repository.CurrencyRepository;
import com.example.gymsaas.common.exception.ApiException;
import com.example.gymsaas.i18n.repository.LanguageRepository;
import com.example.gymsaas.identity.entity.Role;
import com.example.gymsaas.identity.entity.User;
import com.example.gymsaas.identity.repository.RoleRepository;
import com.example.gymsaas.identity.repository.UserRepository;
import com.example.gymsaas.tenant.dto.CreateTenantRequest;
import com.example.gymsaas.tenant.dto.TenantResponse;
import com.example.gymsaas.tenant.entity.Tenant;
import com.example.gymsaas.tenant.repository.TenantRepository;
import com.example.gymsaas.tenant.service.TenantService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CurrencyRepository currencyRepository;
    private final LanguageRepository languageRepository;

    public TenantServiceImpl(TenantRepository tenantRepository,
                         UserRepository userRepository,
                         RoleRepository roleRepository,
                         CurrencyRepository currencyRepository,
                         LanguageRepository languageRepository) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.currencyRepository = currencyRepository;
        this.languageRepository = languageRepository;
    }

    @Transactional
    public TenantResponse signUpGym(CreateTenantRequest request, String externalIdentityId) {
        String currency = request.defaultCurrencyCode().toUpperCase();
        String language = request.defaultLanguageCode().toLowerCase();

        // 1. Check everything first
        if (tenantRepository.existsBySlug(request.slug())) {
            throw new ApiException(HttpStatus.CONFLICT, "A gym with this slug already exists");
        }
        if (!currencyRepository.existsById(currency)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Unknown currency: " + currency);
        }
        if (!languageRepository.existsById(language)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Unknown language: " + language);
        }
        if (userRepository.findByExternalIdentityId(externalIdentityId).isPresent()) {
            throw new ApiException(HttpStatus.CONFLICT, "You already have an account");
        }
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new IllegalStateException("ADMIN role is missing. Check the seed migration."));

        // 2. Create the gym
        Tenant tenant = new Tenant();
        tenant.setName(request.gymName());
        tenant.setSlug(request.slug());
        tenant.setDefaultCurrencyCode(currency);
        tenant.setDefaultLanguageCode(language);
        tenant = tenantRepository.save(tenant);

        // 3. Create the owner as the gym's Admin, linked to their Thunder ID
        User admin = new User();
        admin.setTenantId(tenant.getId());
        admin.setEmail(request.adminEmail());
        admin.setFirstName(request.adminFirstName());
        admin.setLastName(request.adminLastName());
        admin.setExternalIdentityId(externalIdentityId);
        admin.setPreferredLanguageCode(language);
        admin.getRoles().add(adminRole);
        admin = userRepository.save(admin);

        return new TenantResponse(tenant.getId(), tenant.getName(), tenant.getSlug(),
                admin.getId(), admin.getEmail());
    }
}