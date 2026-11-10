package com.example.gymsaas.common.security;

import com.example.gymsaas.identity.entity.Permission;
import com.example.gymsaas.identity.entity.Role;
import com.example.gymsaas.identity.entity.User;
import com.example.gymsaas.identity.enums.UserStatus;
import com.example.gymsaas.identity.repository.UserRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ThunderJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserRepository userRepository;

    public ThunderJwtAuthenticationConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        User user = userRepository.findWithRolesByExternalIdentityId(jwt.getSubject())
                .orElseThrow(() -> new BadCredentialsException("No user is linked to this identity"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new DisabledException("User account is not active");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            for (Permission permission : role.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(permission.getCode()));
            }
        }

        CurrentUser currentUser = new CurrentUser(user.getId(), user.getTenantId(), user.getEmail());
        return new AppAuthenticationToken(jwt, authorities, currentUser);
    }
}