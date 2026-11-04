package be.kdg.ipj3.platformbackend.shared.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        if (jwt.getClaims() == null) {
            return List.of();
        }
        
        final Object realmAccessObj = jwt.getClaims().get("realm_access");
        if (!(realmAccessObj instanceof Map)) {
            return List.of();
        }
        
        @SuppressWarnings("unchecked")
        final var realmAccess = (Map<String, Object>) realmAccessObj;
        
        final var rolesObj = realmAccess.get("roles");
        if (!(rolesObj instanceof List)) {
            return List.of();
        }
        
        @SuppressWarnings("unchecked")
        final var roles = (List<String>) rolesObj;
        
        if (roles.isEmpty()) {
            return List.of();
        }
        
        return roles.stream()
                .filter(role -> role != null && !role.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}