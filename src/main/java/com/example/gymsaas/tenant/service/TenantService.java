package com.example.gymsaas.tenant.service;

import com.example.gymsaas.tenant.dto.CreateTenantRequest;
import com.example.gymsaas.tenant.dto.TenantResponse;

public interface TenantService {

    TenantResponse signUpGym(CreateTenantRequest request, String externalIdentityId);
}