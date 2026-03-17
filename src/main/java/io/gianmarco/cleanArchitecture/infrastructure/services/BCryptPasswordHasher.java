package io.gianmarco.cleanArchitecture.infrastructure.services;

import io.gianmarco.cleanArchitecture.application.services.PasswordHasher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptPasswordHasher implements PasswordHasher {

    private final PasswordEncoder encoder;

    public BCryptPasswordHasher(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public String hash(String value) {
        return encoder.encode(value);
    }

    public boolean compare(String value, String hash) {
        return encoder.matches(value, hash);
    }
}
