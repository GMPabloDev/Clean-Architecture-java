package io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth;

import io.gianmarco.cleanArchitecture.application.ports.auth.resendOtp.ResendOtpInput;
import io.gianmarco.cleanArchitecture.application.ports.auth.resendOtp.ResendOtpOutput;

public interface ResendOtpUseCase {
    ResendOtpOutput execute(ResendOtpInput input);
}
