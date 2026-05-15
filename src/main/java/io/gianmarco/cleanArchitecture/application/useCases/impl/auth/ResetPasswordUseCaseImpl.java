package io.gianmarco.cleanArchitecture.application.useCases.impl.auth;

import io.gianmarco.cleanArchitecture.application.ports.auth.resetPassword.ResetPasswordInput;
import io.gianmarco.cleanArchitecture.application.ports.auth.resetPassword.ResetPasswordOutput;
import io.gianmarco.cleanArchitecture.application.services.OtpGenerator;
import io.gianmarco.cleanArchitecture.application.services.PasswordHasher;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.ResetPasswordUseCase;
import io.gianmarco.cleanArchitecture.domain.entities.Otp;
import io.gianmarco.cleanArchitecture.domain.entities.OtpType;
import io.gianmarco.cleanArchitecture.domain.entities.User;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.InvalidOtpException;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.OtpExpiredException;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.OtpMaxAttemptsExceededException;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.OtpNotFoundException;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.UserNotFoundException;
import io.gianmarco.cleanArchitecture.domain.repositories.otp.OtpRepository;
import io.gianmarco.cleanArchitecture.domain.repositories.refreshToken.RefreshTokenRepository;
import io.gianmarco.cleanArchitecture.domain.repositories.user.UserRepository;

public class ResetPasswordUseCaseImpl implements ResetPasswordUseCase {

    private static final int MAX_ATTEMPTS = 5;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OtpRepository otpRepository;
    private final OtpGenerator otpGenerator;
    private final PasswordHasher passwordHasher;

    public ResetPasswordUseCaseImpl(
        UserRepository userRepository,
        RefreshTokenRepository refreshTokenRepository,
        OtpRepository otpRepository,
        OtpGenerator otpGenerator,
        PasswordHasher passwordHasher
    ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.otpRepository = otpRepository;
        this.otpGenerator = otpGenerator;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public ResetPasswordOutput execute(ResetPasswordInput input) {
        String normalizedEmail = input.email().trim().toLowerCase();

        User user = userRepository
            .findByEmail(input.email())
            .orElseThrow(() -> new UserNotFoundException(normalizedEmail));

        Otp otp = otpRepository
            .findLatestByEmailAndType(user.getEmail(), OtpType.PASSWORD_RESET)
            .orElseThrow(OtpNotFoundException::new);

        if (otp.isExpired()) {
            otpRepository.deleteById(otp.getId());
            throw new OtpExpiredException();
        }

        if (otp.getAttempts() >= MAX_ATTEMPTS) {
            otpRepository.deleteById(otp.getId());
            throw new OtpMaxAttemptsExceededException();
        }

        boolean isValid = otpGenerator.verify(input.otp(), otp.getOtpHash());

        if (!isValid) {
            otp.increaseAttempts();
            otpRepository.save(otp);

            int attemptsLeft = otp.attemptsLeft(MAX_ATTEMPTS);

            throw new InvalidOtpException(attemptsLeft);
        }

        String hashedPassword = passwordHasher.hash(input.newPassword());
        user.changePassword(hashedPassword); // 🔥 dominio
        userRepository.save(user); // 🔥 persistencia

        otpRepository.deleteById(otp.getId());
        refreshTokenRepository.deleteAllByUser(user.getId());

        return new ResetPasswordOutput(
            "Password updated successfully",
            "Contraseña actualizada correctamente"
        );
    }
}
