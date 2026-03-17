package io.gianmarco.cleanArchitecture.application.services;

public interface OtpGenerator {
    String generate(int length);
    String hash(String otp);
    boolean verify(String otp, String hashedOtp);
}
