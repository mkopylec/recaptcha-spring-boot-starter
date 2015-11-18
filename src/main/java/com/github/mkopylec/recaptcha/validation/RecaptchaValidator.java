package com.github.mkopylec.recaptcha.validation;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import org.slf4j.Logger;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

import static org.slf4j.LoggerFactory.getLogger;

public class RecaptchaValidator {

    private static final Logger log = getLogger(RecaptchaValidator.class);

    private final RestTemplate restTemplate;
    private final RecaptchaProperties recaptcha;

    public RecaptchaValidator(RestTemplate restTemplate, RecaptchaProperties recaptcha) {
        this.restTemplate = restTemplate;
        this.recaptcha = recaptcha;
    }

    public ValidationResult validate(HttpServletRequest request) {
        String ipAddress = recaptcha.getRemoteIpHeader() != null
                ? request.getHeader(recaptcha.getRemoteIpHeader())
                : request.getRemoteAddr();
        return validate(request.getParameter(recaptcha.getResponseParameter()), ipAddress);
    }

    public ValidationResult validate(String userResponse) {
        return validate(userResponse, null);
    }

    public ValidationResult validate(String userResponse, String ipAddress) {
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("secret", recaptcha.getSecretKey());
        parameters.add("response", userResponse);
        parameters.add("remoteip", ipAddress);

        log.debug("Validating reCAPTCHA:\n    verification url: {}\n    verification parameters: {}", recaptcha.getVerificationUrl(), parameters);

        try {
            ValidationResult result = restTemplate.postForEntity(recaptcha.getVerificationUrl(), parameters, ValidationResult.class).getBody();
            log.debug("reCAPTCHA validation finished: {}", result);
            return result;
        } catch (RestClientException ex) {
            throw new RecaptchaValidationException(userResponse, recaptcha.getVerificationUrl(), ex);
        }
    }
}
