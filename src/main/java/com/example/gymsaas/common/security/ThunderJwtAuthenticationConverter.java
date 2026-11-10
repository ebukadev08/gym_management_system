package com.example.gymsaas.common.security;

import com.example.gymsaas.identity.entity.Permission;
import com.example.gymsaas.identity.entity.Role;
import com.example.gymsaas.identity.entity.User;
import com.example.gymsaas.identity.enums.UserStatus;
import com.example.gymsaas.identity.repository.UserRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class ThunderJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserRepository userRepository;

    public ThunderJwtAuthenticationConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Optional<User> found = userRepository.findWithRolesByExternalIdentityId(jwt.getSubject());

        // A real Thunder ID account with no gym account yet.
        // It may only sign up a gym (see SecurityConfig).
        if (found.isEmpty()) {
            return new AppAuthenticationToken(jwt, List.of(new SimpleGrantedAuthority("ROLE_UNREGISTERED")), null);
        }
        User user = found.get();
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