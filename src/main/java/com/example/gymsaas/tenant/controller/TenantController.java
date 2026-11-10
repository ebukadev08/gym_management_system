package com.example.gymsaas.tenant.controller;

import com.example.gymsaas.common.api.ApiRes;
import com.example.gymsaas.common.security.AppAuthenticationToken;
import com.example.gymsaas.tenant.dto.CreateTenantRequest;
import com.example.gymsaas.tenant.dto.TenantResponse;
import com.example.gymsaas.tenant.service.TenantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiRes<TenantResponse>> signUp(@Valid @RequestBody CreateTenantRequest request,
                                                         Authentication authentication) {
        AppAuthenticationToken token = (AppAuthenticationToken) authentication;
        String thunderId = token.getToken().getSubject();   // the owner's id from Thunder ID
        TenantResponse response = tenantService.signUpGym(request, thunderId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiRes.success("Gym created successfully", response));
    }
}