package io.gianmarco.cleanArchitecture.application.services;

public interface PasswordHasher {
    String hash(String value);
    boolean compare(String value, String hash);
}
