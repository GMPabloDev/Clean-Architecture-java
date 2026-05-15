package io.gianmarco.cleanArchitecture.application.useCases.impl.auth;

import io.gianmarco.cleanArchitecture.application.ports.auth.resendOtp.ResendOtpInput;
import io.gianmarco.cleanArchitecture.application.ports.auth.resendOtp.ResendOtpOutput;
import io.gianmarco.cleanArchitecture.application.services.EmailSender;
import io.gianmarco.cleanArchitecture.application.services.OtpGenerator;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.ResendOtpUseCase;
import io.gianmarco.cleanArchitecture.domain.entities.Otp;
import io.gianmarco.cleanArchitecture.domain.entities.OtpType;
import io.gianmarco.cleanArchitecture.domain.entities.User;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.EmailAlreadyVerifiedException;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.OtpCooldownException;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.UserNotFoundException;
import io.gianmarco.cleanArchitecture.domain.repositories.otp.OtpRepository;
import io.gianmarco.cleanArchitecture.domain.repositories.user.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ResendOtpUseCaseImpl implements ResendOtpUseCase {

    private static final int RESEND_COOLDOWN_MINUTES = 3;
    private static final int OTP_EXPIRATION_MINUTES = 15;

    public final UserRepository userRepository;
    public final OtpRepository otpRepository;
    public final OtpGenerator otpGenerator;
    public final EmailSender emailSender;

    public ResendOtpUseCaseImpl(
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
    public ResendOtpOutput execute(ResendOtpInput input) {
        String normalizedEmail = input.email().trim().toLowerCase();
        OtpType type = input.type();

        User user = userRepository
            .findByEmail(input.email())
            .orElseThrow(() -> new UserNotFoundException(normalizedEmail));

        if (type == OtpType.EMAIL_VERIFICATION && user.isEmailVerified()) {
            throw new EmailAlreadyVerifiedException();
        }

        otpRepository
            .findLatestByEmailAndType(normalizedEmail, type)
            .ifPresent(lastOtp -> {
                if (lastOtp.isInCooldown(RESEND_COOLDOWN_MINUTES)) {
                    long secondsLeft = lastOtp.secondsUntilCooldownEnds(
                        RESEND_COOLDOWN_MINUTES
                    );
                    throw new OtpCooldownException(secondsLeft);
                }
            });

        String otp = otpGenerator.generate(6);
        String otpHash = otpGenerator.hash(otp);

        Instant expiresAt = Instant.now().plus(
            OTP_EXPIRATION_MINUTES,
            ChronoUnit.MINUTES
        );

        // 5. Eliminar OTPs anteriores
        otpRepository.deleteByOwner(normalizedEmail, type);

        Otp newOtp = Otp.create(
            user.getId(),
            normalizedEmail,
            otpHash,
            type,
            expiresAt
        );

        otpRepository.save(newOtp);

        String subject = (type == OtpType.EMAIL_VERIFICATION)
            ? "Verify Your Email - Resend"
            : "Reset Your Password - OTP Code";

        String body = buildEmailTemplate(type, otp, user);

        try {
            emailSender.send(normalizedEmail, subject, body);
        } catch (Exception e) {
            // rollback lógico
            otpRepository.deleteByOwner(normalizedEmail, type);
            throw new RuntimeException("Failed to send email");
        }

        return new ResendOtpOutput(
            "A new code was sent to your email.",
            "Se envió un nuevo código a tu correo."
        );
    }

    private String buildEmailTemplate(OtpType type, String otp, User user) {
        String name = user.getName() != null ? user.getName() : "Usuario";

        if (type == OtpType.EMAIL_VERIFICATION) {
            return "Hola " + name + ", tu código de verificación es: " + otp;
        }

        return (
            "Hola " +
            name +
            ", tu código para restablecer contraseña es: " +
            otp
        );
    }
}
