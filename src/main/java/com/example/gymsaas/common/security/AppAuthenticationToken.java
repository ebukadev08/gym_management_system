package com.example.gymsaas.common.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

public class AppAuthenticationToken extends JwtAuthenticationToken {

    private final CurrentUser currentUser;

    public AppAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities, CurrentUser currentUser) {
        super(jwt, authorities, jwt.getSubject());
        this.currentUser = currentUser;
    }

    public CurrentUser getCurrentUser() {
        return currentUser;
    }
}