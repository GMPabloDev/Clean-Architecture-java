package io.gianmarco.cleanArchitecture.application.services;

public interface EmailSender {
    void send(String to, String subject, String body);
}
