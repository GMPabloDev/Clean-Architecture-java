package io.gianmarco.cleanArchitecture.application.useCases.impl.auth;

import io.gianmarco.cleanArchitecture.application.ports.auth.verifyEmail.VerifyEmailInput;
import io.gianmarco.cleanArchitecture.application.ports.auth.verifyEmail.VerifyEmailOutput;
import io.gianmarco.cleanArchitecture.application.services.OtpGenerator;
import io.gianmarco.cleanArchitecture.application.services.TokenManager;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.VerifyEmailUseCase;
import io.gianmarco.cleanArchitecture.domain.entities.Otp;
import io.gianmarco.cleanArchitecture.domain.entities.OtpType;
import io.gianmarco.cleanArchitecture.domain.entities.RefreshToken;
import io.gianmarco.cleanArchitecture.domain.entities.Session;
import io.gianmarco.cleanArchitecture.domain.entities.User;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.EmailAlreadyVerifiedException;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.InvalidOtpException;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.OtpExpiredException;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.OtpMaxAttemptsExceededException;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.OtpNotFoundException;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.UserNotFoundException;
import io.gianmarco.cleanArchitecture.domain.repositories.otp.OtpRepository;
import io.gianmarco.cleanArchitecture.domain.repositories.refreshToken.RefreshTokenRepository;
import io.gianmarco.cleanArchitecture.domain.repositories.session.SessionRepository;
import io.gianmarco.cleanArchitecture.domain.repositories.user.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class VerifyEmailUseCaseImpl implements VerifyEmailUseCase {

    private static final int MAX_ATTEMPTS = 5;
    private static final long SESSION_DAYS = 7;

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final OtpRepository otpRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OtpGenerator otpGenerator;
    private final TokenManager tokenManager;

    public VerifyEmailUseCaseImpl(
        UserRepository userRepository,
        SessionRepository sessionRepository,
        OtpRepository otpRepository,
        RefreshTokenRepository refreshTokenRepository,
        OtpGenerator otpGenerator,
        TokenManager tokenManager
    ) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.otpRepository = otpRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.otpGenerator = otpGenerator;
        this.tokenManager = tokenManager;
    }

    @Override
    public VerifyEmailOutput execute(VerifyEmailInput input) {
        User user = userRepository
            .findByEmail(input.email())
            .orElseThrow(() -> new UserNotFoundException(input.email()));

        if (user.isEmailVerified()) {
            throw new EmailAlreadyVerifiedException();
        }

        Otp otp = otpRepository
            .findLatestByEmailAndType(input.email(), OtpType.EMAIL_VERIFICATION)
            .orElseThrow(OtpNotFoundException::new);

        if (otp.isExpired()) {
            otpRepository.deleteById(otp.getId());
            throw new OtpExpiredException();
        }

        //if (Instant.now().isAfter(otp.getExpiresAt())) {
        //    otpRepository.deleteById(otp.getId());
        //    throw new OtpExpiredException();
        //}

        // 🔁 5. Intentos máximos
        if (otp.getAttempts() >= MAX_ATTEMPTS) {
            otpRepository.deleteById(otp.getId());
            throw new OtpMaxAttemptsExceededException();
        }

        String hashedInput = otpGenerator.hash(input.otp());
        boolean isValid = otp.verify(hashedInput);

        if (!isValid) {
            otp.increaseAttempts();
            otpRepository.save(otp);

            throw new InvalidOtpException(otp.attemptsLeft(MAX_ATTEMPTS));
        }

        user.verifyEmail();
        userRepository.save(user);
        otpRepository.deleteById(otp.getId());

        // 🔐 8. Generar tokens
        String accessToken = tokenManager.generateAccessToken(user);
        String refreshTokenValue = tokenManager.generateRefreshToken(
            user.getId()
        );

        // 📱 9. Crear sesión
        Instant expiresAt = Instant.now().plus(SESSION_DAYS, ChronoUnit.DAYS);

        Session session = Session.create(user.getId(), expiresAt);
        Session savedSession = sessionRepository.save(session);

        // ♻️ 10. Guardar refresh token
        RefreshToken refreshToken = RefreshToken.create(
            user.getId(),
            savedSession.getId(),
            refreshTokenValue,
            expiresAt
        );

        refreshTokenRepository.save(refreshToken);

        // 📤 response
        return new VerifyEmailOutput(accessToken, refreshTokenValue);
    }
}
