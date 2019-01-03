package com.github.mkopylec.recaptcha.webflux.test.validation;

import com.github.mkopylec.recaptcha.commons.validation.ValidationResult;
import com.github.mkopylec.recaptcha.webflux.validation.RecaptchaValidator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.github.mkopylec.recaptcha.test.specification.BasicSpec.REMOTE_IP_ADDRESS;
import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(value = "testValidation")
public class ValidationController {

    private static final Logger log = getLogger(ValidationController.class);

    @Autowired
    private RecaptchaValidator recaptchaValidator;

    @PostMapping("userResponse")
    public Mono<ValidationResult> validateUsingUserResponse(ServerWebExchange exchange) {
        return recaptchaValidator.validate(exchange)
                .doOnError(ex -> log.error(ex.getMessage(), ex));
    }

    @PostMapping("userResponseAndIp")
    public Mono<ValidationResult> validateUsingUserResponseAndIp(ServerWebExchange exchange) {
        return recaptchaValidator.validate(exchange, REMOTE_IP_ADDRESS)
                .doOnError(ex -> log.error(ex.getMessage(), ex));
    }
}
