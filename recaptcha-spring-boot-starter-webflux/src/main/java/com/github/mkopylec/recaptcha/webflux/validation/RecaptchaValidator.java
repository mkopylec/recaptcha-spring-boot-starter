package com.github.mkopylec.recaptcha.webflux.validation;

import com.github.mkopylec.recaptcha.commons.RecaptchaProperties;
import com.github.mkopylec.recaptcha.commons.RecaptchaProperties.Validation;
import com.github.mkopylec.recaptcha.commons.validation.RecaptchaValidationException;
import com.github.mkopylec.recaptcha.commons.validation.ValidationResult;
import org.slf4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

public class RecaptchaValidator {

    private static final Logger log = getLogger(RecaptchaValidator.class);

    protected final WebClient webClient;
    protected final Validation validation;

    public RecaptchaValidator(WebClient webClient, RecaptchaProperties recaptcha) {
        this.webClient = webClient;
        validation = recaptcha.getValidation();
    }

    public Mono<ValidationResult> validate(ServerWebExchange exchange) {
        return validate(exchange, getIpAddress(exchange));
    }

    public Mono<ValidationResult> validate(ServerWebExchange exchange, String ipAddress) {
        return validate(getUserResponse(exchange), ipAddress);
    }

    public Mono<ValidationResult> validate(ServerWebExchange exchange, String ipAddress, String secretKey) {
        return validate(getUserResponse(exchange), ipAddress, secretKey);
    }

    public Mono<ValidationResult> validate(Mono<Optional<String>> userResponse) {
        return validate(userResponse, null);
    }

    public Mono<ValidationResult> validate(Mono<Optional<String>> userResponse, String ipAddress) {
        return validate(userResponse, ipAddress, validation.getSecretKey());
    }

    public Mono<ValidationResult> validate(Mono<Optional<String>> userResponse, String ipAddress, String secretKey) {
        Mono<MultiValueMap<String, Object>> body = userResponse
                .map(response -> {
                    MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
                    parameters.add("secret", secretKey);
                    parameters.add("response", response.orElse(null));
                    parameters.add("remoteip", ipAddress);
                    return parameters;
                })
                .doOnSuccess(parameters -> log.debug("Validating reCAPTCHA:\n    verification url: {}\n    verification parameters: {}", validation.getVerificationUrl(), parameters));
        return webClient
                .post()
                .uri(validation.getVerificationUrl())
                .body(body, new ParameterizedTypeReference<MultiValueMap<String, Object>>() {

                })
                .retrieve()
                .bodyToMono(ValidationResult.class)
                .doOnSuccess(result -> log.debug("reCAPTCHA validation finished: {}", result))
                .doOnError(Exception.class, ex -> {
                    throw new RecaptchaValidationException(validation.getVerificationUrl(), ex);
                });
    }

    protected String getIpAddress(ServerWebExchange exchange) {
        InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
        return remoteAddress != null ? remoteAddress.getAddress().toString() : null;
    }

    protected Mono<Optional<String>> getUserResponse(ServerWebExchange exchange) {
        return exchange.getFormData()
                .map(parameters -> ofNullable(parameters.getFirst(validation.getResponseParameter())));
    }
}
