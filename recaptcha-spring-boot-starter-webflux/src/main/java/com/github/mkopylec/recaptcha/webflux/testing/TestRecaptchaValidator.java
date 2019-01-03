package com.github.mkopylec.recaptcha.webflux.testing;

import com.github.mkopylec.recaptcha.commons.RecaptchaProperties;
import com.github.mkopylec.recaptcha.commons.RecaptchaProperties.Testing;
import com.github.mkopylec.recaptcha.commons.validation.ValidationResult;
import com.github.mkopylec.recaptcha.webflux.validation.RecaptchaValidator;
import org.slf4j.Logger;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Mono.just;

public class TestRecaptchaValidator extends RecaptchaValidator {

    private static final Logger log = getLogger(TestRecaptchaValidator.class);

    protected final Testing testing;

    public TestRecaptchaValidator(RecaptchaProperties recaptcha) {
        super(null, recaptcha);
        testing = recaptcha.getTesting();
    }

    @Override
    public Mono<ValidationResult> validate(ServerWebExchange exchange) {
        return getValidationResult();
    }

    @Override
    public Mono<ValidationResult> validate(ServerWebExchange exchange, String ipAddress) {
        return getValidationResult();
    }

    @Override
    public Mono<ValidationResult> validate(ServerWebExchange exchange, String ipAddress, String secretKey) {
        return getValidationResult();
    }

    @Override
    public Mono<ValidationResult> validate(Mono<Optional<String>> userResponse) {
        return getValidationResult();
    }

    @Override
    public Mono<ValidationResult> validate(Mono<Optional<String>> userResponse, String ipAddress) {
        return getValidationResult();
    }

    @Override
    public Mono<ValidationResult> validate(Mono<Optional<String>> userResponse, String ipAddress, String secretKey) {
        return getValidationResult();
    }

    protected Mono<ValidationResult> getValidationResult() {
        Mono<ValidationResult> result = just(new ValidationResult(false, testing.getResultErrorCodes()));
        if (testing.isSuccessResult()) {
            result = just(new ValidationResult(true, new ArrayList<>()));
        }
        return result.doOnSuccess(res -> log.debug("reCAPTCHA validation finished: {}", res));
    }
}
