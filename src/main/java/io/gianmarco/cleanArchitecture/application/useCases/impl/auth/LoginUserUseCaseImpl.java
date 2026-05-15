package io.gianmarco.cleanArchitecture.application.useCases.impl.auth;

import io.gianmarco.cleanArchitecture.application.ports.auth.login.LoginUserInput;
import io.gianmarco.cleanArchitecture.application.ports.auth.login.LoginUserOutput;
import io.gianmarco.cleanArchitecture.application.services.PasswordHasher;
import io.gianmarco.cleanArchitecture.application.services.TokenManager;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.LoginUserUseCase;
import io.gianmarco.cleanArchitecture.domain.entities.RefreshToken;
import io.gianmarco.cleanArchitecture.domain.entities.Session;
import io.gianmarco.cleanArchitecture.domain.entities.User;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.CredentialsInvalidException;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.EmailUnverifiedException;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.UserNotFoundException;
import io.gianmarco.cleanArchitecture.domain.repositories.refreshToken.RefreshTokenRepository;
import io.gianmarco.cleanArchitecture.domain.repositories.session.SessionRepository;
import io.gianmarco.cleanArchitecture.domain.repositories.user.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class LoginUserUseCaseImpl implements LoginUserUseCase {

    public final UserRepository userRepository;
    public final SessionRepository sessionRepository;
    public final RefreshTokenRepository refreshTokenRepository;
    public final TokenManager tokenManager;
    public final PasswordHasher passwordHasher;

    public LoginUserUseCaseImpl(
        UserRepository userRepository,
        SessionRepository sessionRepository,
        RefreshTokenRepository refreshTokenRepository,
        TokenManager tokenManager,
        PasswordHasher passwordHasher
    ) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenManager = tokenManager;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public LoginUserOutput execute(LoginUserInput input) {
        User user = userRepository
            .findByEmail(input.email())
            .orElseThrow(() -> new UserNotFoundException(input.email()));

        if (!user.isEmailVerified()) {
            throw new EmailUnverifiedException();
        }

        Boolean isPasswordValid = this.passwordHasher.compare(
            input.password(),
            user.getPassword()
        );

        if (!isPasswordValid) {
            throw new CredentialsInvalidException();
        }

        String accessToken = tokenManager.generateAccessToken(user);
        String refreshTokenValue = tokenManager.generateRefreshToken(
            user.getId()
        );

        Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);

        Session session = Session.create(user.getId(), expiresAt);
        Session savedSession = sessionRepository.save(session);

        RefreshToken refreshToken = RefreshToken.create(
            user.getId(),
            savedSession.getId(),
            refreshTokenValue,
            expiresAt
        );

        refreshTokenRepository.save(refreshToken);

        return new LoginUserOutput(accessToken, refreshTokenValue);
    }
}
