package io.gianmarco.cleanArchitecture.presentation.controllers.auth;

import io.gianmarco.cleanArchitecture.application.ports.auth.UserOutput;
import io.gianmarco.cleanArchitecture.application.ports.auth.register.RegisterUserInput;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.CreateUserUseCase;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.GetCurrentUserUseCase;
import io.gianmarco.cleanArchitecture.presentation.dtos.ApiResponse;
import io.gianmarco.cleanArchitecture.presentation.dtos.auth.RegisterUserRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final CreateUserUseCase createUserUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;

    public AuthController(
        CreateUserUseCase createUserUseCase,
        GetCurrentUserUseCase getCurrentUserUseCase
    ) {
        this.createUserUseCase = createUserUseCase;
        this.getCurrentUserUseCase = getCurrentUserUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> register(
        @Valid @RequestBody RegisterUserRequest request
    ) {
        var input = new RegisterUserInput(
            request.name(),
            request.email(),
            request.password()
        );
        var output = createUserUseCase.execute(input);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(output));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserOutput>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder
            .getContext()
            .getAuthentication();

        String userId = authentication.getName();

        UserOutput output = getCurrentUserUseCase.execute(userId);

        return ResponseEntity.ok(ApiResponse.success(output));
    }
}
