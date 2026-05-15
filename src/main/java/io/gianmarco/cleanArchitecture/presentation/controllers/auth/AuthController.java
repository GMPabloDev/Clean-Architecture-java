package io.gianmarco.cleanArchitecture.presentation.controllers.auth;

import io.gianmarco.cleanArchitecture.application.ports.auth.forgotPassword.ForgotPasswordInput;
import io.gianmarco.cleanArchitecture.application.ports.auth.login.LoginUserInput;
import io.gianmarco.cleanArchitecture.application.ports.auth.register.RegisterUserInput;
import io.gianmarco.cleanArchitecture.application.ports.auth.resendOtp.ResendOtpInput;
import io.gianmarco.cleanArchitecture.application.ports.auth.resetPassword.ResetPasswordInput;
import io.gianmarco.cleanArchitecture.application.ports.auth.verifyEmail.VerifyEmailInput;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.CreateUserUseCase;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.ForgotPasswordUseCase;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.GetCurrentUserUseCase;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.LoginUserUseCase;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.ResendOtpUseCase;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.ResetPasswordUseCase;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.VerifyEmailUseCase;
import io.gianmarco.cleanArchitecture.presentation.dtos.ApiResponse;
import io.gianmarco.cleanArchitecture.presentation.dtos.auth.ForgotPasswordRequest;
import io.gianmarco.cleanArchitecture.presentation.dtos.auth.LoginUserRequest;
import io.gianmarco.cleanArchitecture.presentation.dtos.auth.RegisterUserRequest;
import io.gianmarco.cleanArchitecture.presentation.dtos.auth.ResendOtpRequest;
import io.gianmarco.cleanArchitecture.presentation.dtos.auth.ResetPasswordRequest;
import io.gianmarco.cleanArchitecture.presentation.dtos.auth.VerifyEmailRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final CreateUserUseCase createUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final VerifyEmailUseCase verifyEmailUseCase;
    private final ForgotPasswordUseCase forgotPasswordUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final ResendOtpUseCase resendOtpUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;

    public AuthController(
        CreateUserUseCase createUserUseCase,
        LoginUserUseCase loginUserUseCase,
        VerifyEmailUseCase verifyEmailUseCase,
        ForgotPasswordUseCase forgotPasswordUseCase,
        ResetPasswordUseCase resetPasswordUseCase,
        ResendOtpUseCase resendOtpUseCase,
        GetCurrentUserUseCase getCurrentUserUseCase
    ) {
        this.createUserUseCase = createUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
        this.verifyEmailUseCase = verifyEmailUseCase;
        this.forgotPasswordUseCase = forgotPasswordUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
        this.resendOtpUseCase = resendOtpUseCase;
        this.getCurrentUserUseCase = getCurrentUserUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(
        @Valid @RequestBody RegisterUserRequest request
    ) {
        var input = new RegisterUserInput(request.name(), request.email(), request.password());
        var output = createUserUseCase.execute(input);
        return ResponseEntity.status(201).body(ApiResponse.success(output));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(
        @Valid @RequestBody LoginUserRequest request
    ) {
        var input = new LoginUserInput(request.email(), request.password());
        var output = loginUserUseCase.execute(input);
        return ResponseEntity.ok(ApiResponse.success(output));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<?>> verifyEmail(
        @Valid @RequestBody VerifyEmailRequest request
    ) {
        var input = new VerifyEmailInput(request.email(), request.otp());
        var output = verifyEmailUseCase.execute(input);
        return ResponseEntity.ok(ApiResponse.success(output));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<?>> forgotPassword(
        @Valid @RequestBody ForgotPasswordRequest request
    ) {
        var input = new ForgotPasswordInput(request.email());
        var output = forgotPasswordUseCase.execute(input);
        return ResponseEntity.ok(ApiResponse.success(output));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<?>> resetPassword(
        @Valid @RequestBody ResetPasswordRequest request
    ) {
        var input = new ResetPasswordInput(request.email(), request.otp(), request.newPassword());
        var output = resetPasswordUseCase.execute(input);
        return ResponseEntity.ok(ApiResponse.success(output));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<?>> resendOtp(
        @Valid @RequestBody ResendOtpRequest request
    ) {
        var input = new ResendOtpInput(request.email(), request.type());
        var output = resendOtpUseCase.execute(input);
        return ResponseEntity.ok(ApiResponse.success(output));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> me(Authentication authentication) {
        var output = getCurrentUserUseCase.execute(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(output));
    }
}
