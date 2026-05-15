package io.gianmarco.cleanArchitecture.application.useCases.impl.auth;

import io.gianmarco.cleanArchitecture.application.ports.auth.forgotPassword.ForgotPasswordInput;
import io.gianmarco.cleanArchitecture.application.ports.auth.forgotPassword.ForgotPasswordOutput;
import io.gianmarco.cleanArchitecture.application.services.EmailSender;
import io.gianmarco.cleanArchitecture.application.services.OtpGenerator;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.ForgotPasswordUseCase;
import io.gianmarco.cleanArchitecture.domain.entities.Otp;
import io.gianmarco.cleanArchitecture.domain.entities.OtpType;
import io.gianmarco.cleanArchitecture.domain.entities.User;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.OtpCooldownException;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.UserNotFoundException;
import io.gianmarco.cleanArchitecture.domain.repositories.otp.OtpRepository;
import io.gianmarco.cleanArchitecture.domain.repositories.user.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ForgotPasswordUseCaseImpl implements ForgotPasswordUseCase {

    private static final int OTP_EXPIRATION_MINUTES = 15;
    private static final int RESEND_COOLDOWN_MINUTES = 5;

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final OtpGenerator otpGenerator;
    private final EmailSender emailSender;

    public ForgotPasswordUseCaseImpl(
        UserRepository userRepository,
        OtpRepository otpRepository,
        OtpGenerator otpGenerator,
        EmailSender emailSender
    ) {
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.otpGenerator = otpGenerator;
        this.emailSender = emailSender;
    }

    @Override
    public ForgotPasswordOutput execute(ForgotPasswordInput input) {
        String normalizedEmail = input.email().trim().toLowerCase();

        // 1. Buscar usuario
        User user = userRepository
            .findByEmail(normalizedEmail)
            .orElseThrow(() -> new UserNotFoundException(normalizedEmail));

        // 2. Verificar cooldown usando lógica del dominio (DDD 🔥)
        otpRepository
            .findLatestByEmailAndType(normalizedEmail, OtpType.PASSWORD_RESET)
            .ifPresent(lastOtp -> {
                if (lastOtp.isInCooldown(RESEND_COOLDOWN_MINUTES)) {
                    long secondsLeft = lastOtp.secondsUntilCooldownEnds(
                        RESEND_COOLDOWN_MINUTES
                    );
                    throw new OtpCooldownException(secondsLeft);
                }
            });

        // 3. Generar OTP
        String otp = otpGenerator.generate(6);
        String otpHash = otpGenerator.hash(otp);

        Instant expiresAt = Instant.now().plus(
            OTP_EXPIRATION_MINUTES,
            ChronoUnit.MINUTES
        );

        // 4. Eliminar OTPs anteriores
        otpRepository.deleteByOwner(user.getEmail(), OtpType.PASSWORD_RESET);

        // 5. Crear nuevo OTP
        Otp newOtp = Otp.create(
            user.getId(),
            normalizedEmail,
            otpHash,
            OtpType.PASSWORD_RESET,
            expiresAt
        );

        otpRepository.save(newOtp);

        // 6. Enviar email
        String subject = "Password Recovery";
        String body = buildEmailTemplate(otp, user);

        try {
            emailSender.send(normalizedEmail, subject, body);

            return new ForgotPasswordOutput(
                "A code was sent to your email to confirm your identity!",
                "Se envió un código a tu correo electrónico para confirmar tu identidad."
            );
        } catch (Exception e) {
            // rollback lógico
            otpRepository.deleteByOwner(
                user.getEmail(),
                OtpType.PASSWORD_RESET
            );
            throw new RuntimeException("Failed to send recovery email");
        }
    }

    private String buildEmailTemplate(String otp, User user) {
        String name = user.getName() != null ? user.getName() : "User";
        return "Hello " + name + ", your recovery code is: " + otp;
    }
}
