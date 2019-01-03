package com.github.mkopylec.recaptcha.webflux.test.testing;

import com.github.mkopylec.recaptcha.commons.validation.ValidationResult;
import com.github.mkopylec.recaptcha.webflux.validation.RecaptchaValidator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(value = "testTesting")
public class TestingController {

    private static final Logger log = getLogger(TestingController.class);

    @Autowired
    private RecaptchaValidator recaptchaValidator;

    @PostMapping("validate")
    public Mono<ValidationResult> validate(ServerWebExchange exchange) {
        return recaptchaValidator.validate(exchange)
                .doOnError(ex -> log.error(ex.getMessage(), ex));
    }
}
