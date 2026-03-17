package io.gianmarco.cleanArchitecture.application.ports.auth;

public record UserOutput(
    Long id,
    String name,
    String email,
    String password) {

}
