// infrastructure/security/UserDetailsServiceImpl.java
package io.gianmarco.cleanArchitecture.infrastructure.security;

import io.gianmarco.cleanArchitecture.domain.repositories.user.UserRepository;
import java.util.UUID;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Spring lo llama con username = userId (UUID como String)
    @Override
    public UserDetails loadUserByUsername(String userId)
        throws UsernameNotFoundException {
        var domainUser = userRepository
            .findById(UUID.fromString(userId))
            .orElseThrow(() ->
                new UsernameNotFoundException("User not found: " + userId)
            );

        var authorities = domainUser
            .getRoles()
            .stream()
            .map(role ->
                new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())
            )
            .toList();

        return User.builder()
            .username(domainUser.getId().toString())
            .password(
                domainUser.getPassword() != null ? domainUser.getPassword() : ""
            )
            .authorities(authorities)
            .accountLocked(domainUser.isDisabled())
            .build();
    }
}
