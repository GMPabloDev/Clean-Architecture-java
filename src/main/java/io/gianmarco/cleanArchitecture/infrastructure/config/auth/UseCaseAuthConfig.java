package io.gianmarco.cleanArchitecture.infrastructure.config.auth;

import io.gianmarco.cleanArchitecture.application.services.EmailSender;
import io.gianmarco.cleanArchitecture.application.services.OtpGenerator;
import io.gianmarco.cleanArchitecture.application.services.PasswordHasher;
import io.gianmarco.cleanArchitecture.application.useCases.impl.auth.CreateUserUseCaseImpl;
import io.gianmarco.cleanArchitecture.application.useCases.impl.auth.GetCurrentUserUseCaseImpl;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.CreateUserUseCase;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.GetCurrentUserUseCase;
import io.gianmarco.cleanArchitecture.domain.repositories.otp.OtpRepository;
import io.gianmarco.cleanArchitecture.domain.repositories.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseAuthConfig {

    @Bean
    public GetCurrentUserUseCase getCurrentUserUseCase(UserRepository userRepository) {
        return new GetCurrentUserUseCaseImpl(userRepository);
    }

    @Bean
    public CreateUserUseCase createUserUseCase(
        UserRepository userRepository,
        OtpRepository otpRepository,
        OtpGenerator otpGenerator,
        PasswordHasher passwordHasher,
        EmailSender emailSender
    ) {
        return new CreateUserUseCaseImpl(
            userRepository,
            otpRepository,
            otpGenerator,
            passwordHasher,
            emailSender
        );
    }
}
