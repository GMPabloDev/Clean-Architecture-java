package io.gianmarco.cleanArchitecture.application.useCases.config;

import io.gianmarco.cleanArchitecture.application.services.EmailSender;
import io.gianmarco.cleanArchitecture.application.services.OtpGenerator;
import io.gianmarco.cleanArchitecture.application.services.PasswordHasher;
import io.gianmarco.cleanArchitecture.application.services.TokenManager;
import io.gianmarco.cleanArchitecture.application.useCases.impl.auth.CreateUserUseCaseImpl;
import io.gianmarco.cleanArchitecture.application.useCases.impl.auth.ForgotPasswordUseCaseImpl;
import io.gianmarco.cleanArchitecture.application.useCases.impl.auth.GetCurrentUserUseCaseImpl;
import io.gianmarco.cleanArchitecture.application.useCases.impl.auth.LoginUserUseCaseImpl;
import io.gianmarco.cleanArchitecture.application.useCases.impl.auth.ResendOtpUseCaseImpl;
import io.gianmarco.cleanArchitecture.application.useCases.impl.auth.ResetPasswordUseCaseImpl;
import io.gianmarco.cleanArchitecture.application.useCases.impl.auth.VerifyEmailUseCaseImpl;
import io.gianmarco.cleanArchitecture.application.useCases.impl.category.CreateCategoryUseCaseImpl;
import io.gianmarco.cleanArchitecture.application.useCases.impl.category.ListCategoriesUseCaseImpl;
import io.gianmarco.cleanArchitecture.application.useCases.impl.payments.CreateCheckoutUseCaseImpl;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.CreateUserUseCase;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.ForgotPasswordUseCase;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.GetCurrentUserUseCase;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.LoginUserUseCase;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.ResendOtpUseCase;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.ResetPasswordUseCase;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.VerifyEmailUseCase;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.category.CreateCategoryUseCase;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.category.ListCategoriesUseCase;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.payments.CreateCheckoutUseCase;
import io.gianmarco.cleanArchitecture.domain.repositories.category.CategoryRepository;
import io.gianmarco.cleanArchitecture.domain.repositories.otp.OtpRepository;
import io.gianmarco.cleanArchitecture.domain.repositories.refreshToken.RefreshTokenRepository;
import io.gianmarco.cleanArchitecture.domain.repositories.session.SessionRepository;
import io.gianmarco.cleanArchitecture.domain.repositories.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

    @Bean
    public CreateUserUseCase createUserUseCase(
        UserRepository userRepository,
        OtpRepository otpRepository,
        OtpGenerator otpGenerator,
        PasswordHasher passwordHasher,
        EmailSender emailSender
    ) {
        return new CreateUserUseCaseImpl(userRepository, otpRepository, otpGenerator, passwordHasher, emailSender);
    }

    @Bean
    public GetCurrentUserUseCase getCurrentUserUseCase(UserRepository userRepository) {
        return new GetCurrentUserUseCaseImpl(userRepository);
    }

    @Bean
    public LoginUserUseCase loginUserUseCase(
        UserRepository userRepository,
        SessionRepository sessionRepository,
        RefreshTokenRepository refreshTokenRepository,
        TokenManager tokenManager,
        PasswordHasher passwordHasher
    ) {
        return new LoginUserUseCaseImpl(userRepository, sessionRepository, refreshTokenRepository, tokenManager, passwordHasher);
    }

    @Bean
    public VerifyEmailUseCase verifyEmailUseCase(
        UserRepository userRepository,
        SessionRepository sessionRepository,
        OtpRepository otpRepository,
        RefreshTokenRepository refreshTokenRepository,
        OtpGenerator otpGenerator,
        TokenManager tokenManager
    ) {
        return new VerifyEmailUseCaseImpl(userRepository, sessionRepository, otpRepository, refreshTokenRepository, otpGenerator, tokenManager);
    }

    @Bean
    public ResetPasswordUseCase resetPasswordUseCase(
        UserRepository userRepository,
        RefreshTokenRepository refreshTokenRepository,
        OtpRepository otpRepository,
        OtpGenerator otpGenerator,
        PasswordHasher passwordHasher
    ) {
        return new ResetPasswordUseCaseImpl(userRepository, refreshTokenRepository, otpRepository, otpGenerator, passwordHasher);
    }

    @Bean
    public ResendOtpUseCase resendOtpUseCase(
        UserRepository userRepository,
        OtpRepository otpRepository,
        OtpGenerator otpGenerator,
        EmailSender emailSender
    ) {
        return new ResendOtpUseCaseImpl(userRepository, otpRepository, otpGenerator, emailSender);
    }

    @Bean
    public ForgotPasswordUseCase forgotPasswordUseCase(
        UserRepository userRepository,
        OtpRepository otpRepository,
        OtpGenerator otpGenerator,
        EmailSender emailSender
    ) {
        return new ForgotPasswordUseCaseImpl(userRepository, otpRepository, otpGenerator, emailSender);
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase(CategoryRepository repository) {
        return new CreateCategoryUseCaseImpl(repository);
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase(CategoryRepository repository) {
        return new ListCategoriesUseCaseImpl(repository);
    }

    @Bean
    public CreateCheckoutUseCase createCheckoutUseCase() {
        return new CreateCheckoutUseCaseImpl();
    }
}