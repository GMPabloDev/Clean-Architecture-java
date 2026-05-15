package io.gianmarco.cleanArchitecture.presentation.controllers.payments;

import io.gianmarco.cleanArchitecture.application.ports.payments.checkout.CreateCheckoutInput;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.payments.CreateCheckoutUseCase;
import io.gianmarco.cleanArchitecture.presentation.dtos.payments.CreateCheckoutRequest;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentsController {

    private final CreateCheckoutUseCase createCheckoutUseCase;

    public PaymentsController(CreateCheckoutUseCase createCheckoutUseCase) {
        this.createCheckoutUseCase = createCheckoutUseCase;
    }

    @PostMapping("/checkout-subscription")
    public ResponseEntity<?> checkoutSubscription(
        @Valid @RequestBody CreateCheckoutRequest request
    ) {
        var input = new CreateCheckoutInput(
            request.priceId(),
            request.userId()
        );
        var output = createCheckoutUseCase.execute(input);

        return ResponseEntity.status(201).body(
            Map.of("success", true, "data", output)
        );
    }
}
