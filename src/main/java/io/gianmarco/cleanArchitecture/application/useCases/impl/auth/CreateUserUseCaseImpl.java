package io.gianmarco.cleanArchitecture.application.useCases.impl.auth;

import io.gianmarco.cleanArchitecture.application.ports.auth.register.RegisterUserInput;
import io.gianmarco.cleanArchitecture.application.ports.auth.register.RegisterUserOutput;
import io.gianmarco.cleanArchitecture.application.services.EmailSender;
import io.gianmarco.cleanArchitecture.application.services.OtpGenerator;
import io.gianmarco.cleanArchitecture.application.services.PasswordHasher;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.CreateUserUseCase;
import io.gianmarco.cleanArchitecture.domain.entities.Otp;
import io.gianmarco.cleanArchitecture.domain.entities.OtpType;
import io.gianmarco.cleanArchitecture.domain.entities.User;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.UserAlreadyExistsException;
import io.gianmarco.cleanArchitecture.domain.repositories.otp.OtpRepository;
import io.gianmarco.cleanArchitecture.domain.repositories.user.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private static final long OTP_EXPIRATION_MINUTES = 15;

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final OtpGenerator otpGenerator;
    private final PasswordHasher passwordHasher;
    private final EmailSender emailSender;

    public CreateUserUseCaseImpl(
        UserRepository userRepository,
        OtpRepository otpRepository,
        OtpGenerator otpGenerator,
        PasswordHasher passwordHasher,
        EmailSender emailSender
    ) {
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.otpGenerator = otpGenerator;
        this.passwordHasher = passwordHasher;
        this.emailSender = emailSender;
    }

    @Override
    public RegisterUserOutput execute(RegisterUserInput input) {
        var existingUser = userRepository.findByEmail(input.email());

        if (existingUser.isPresent() && existingUser.get().isEmailVerified()) {
            throw new UserAlreadyExistsException(input.email());
        }

        String otp = otpGenerator.generate(6);
        String otpHash = otpGenerator.hash(otp);
        Instant expiresAt = Instant.now().plus(OTP_EXPIRATION_MINUTES, ChronoUnit.MINUTES);

        String hashedPassword = passwordHasher.hash(input.password());

        User user = User.create(input.name(), input.email(), hashedPassword);
        User savedUser = userRepository.save(user);

        otpRepository.deleteByOwner(input.email(), OtpType.EMAIL_VERIFICATION);

        Otp otpEntity = Otp.create(
            savedUser.getId(),
            input.email(),
            otpHash,
            OtpType.EMAIL_VERIFICATION,
            expiresAt
        );

        otpRepository.save(otpEntity);

        emailSender.send(
            input.email(),
            "Verify Your Email",
            buildEmailTemplate(otp, savedUser.getName())
        );

        return new RegisterUserOutput(
            "An email was sent to confirm your account.",
            "Se envió un correo electrónico para confirmar su cuenta."
        );
    }

    private String buildEmailTemplate(String otp, String name) {
        return (
            "<h1>Hello " +
            name +
            "</h1>" +
            "<p>Your OTP is: <strong>" +
            otp +
            "</strong></p>"
        );
    }
}
