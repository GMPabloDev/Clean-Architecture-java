package io.gianmarco.cleanArchitecture.presentation.controllers.auth;

import io.gianmarco.cleanArchitecture.application.ports.auth.register.RegisterUserInput;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.CreateUserUseCase;
import io.gianmarco.cleanArchitecture.presentation.dtos.auth.RegisterUserRequest;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final CreateUserUseCase createUserUseCase;

    public AuthController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
        @Valid @RequestBody RegisterUserRequest request
    ) {
        var input = new RegisterUserInput(
            request.name(),
            request.email(),
            request.password()
        );
        var output = createUserUseCase.execute(input);

        return ResponseEntity.status(201).body(
            Map.of("success", true, "data", output)
        );
    }
}
