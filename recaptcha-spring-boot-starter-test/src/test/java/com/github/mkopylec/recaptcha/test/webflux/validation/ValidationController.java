package com.github.mkopylec.recaptcha.test.webflux.validation;

import com.github.mkopylec.recaptcha.commons.validation.ValidationResult;
import com.github.mkopylec.recaptcha.webflux.validation.RecaptchaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.github.mkopylec.recaptcha.test.Strings.REMOTE_IP_ADDRESS;
import static com.github.mkopylec.recaptcha.test.Strings.WEBFLUX_SPRING_PROFILE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Profile(WEBFLUX_SPRING_PROFILE)
@RestController
@RequestMapping(value = "testValidation")
public class ValidationController {

    @Autowired
    private RecaptchaValidator recaptchaValidator;

    @RequestMapping(value = "userResponse", method = POST)
    public Mono<ValidationResult> validateUsingUserResponse(ServerWebExchange exchange) {
        return recaptchaValidator.validate(exchange);
    }

    @RequestMapping(value = "userResponseAndIp", method = POST)
    public Mono<ValidationResult> validateUsingUserResponseAndIp(ServerWebExchange exchange) {
        return recaptchaValidator.validate(exchange, REMOTE_IP_ADDRESS);
    }
}
