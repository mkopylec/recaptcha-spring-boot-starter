package com.github.mkopylec.recaptcha.validation;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import com.github.mkopylec.recaptcha.RecaptchaProperties.Validation;
import org.slf4j.Logger;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

import static org.slf4j.LoggerFactory.getLogger;

public class DefaultRecaptchaValidator implements RecaptchaValidator {

    private static final Logger log = getLogger(DefaultRecaptchaValidator.class);

    protected final RestTemplate restTemplate;
    protected final Validation validation;
    protected final IpAddressResolver ipAddressResolver;

    public DefaultRecaptchaValidator(RestTemplate restTemplate, RecaptchaProperties recaptcha, IpAddressResolver ipAddressResolver) {
        this.restTemplate = restTemplate;
        validation = recaptcha.getValidation();
        this.ipAddressResolver = ipAddressResolver;
    }

    @Override
    public ValidationResult validate(HttpServletRequest request) {
        return validate(request, ipAddressResolver.resolveClientIp(request));
    }

    @Override
    public ValidationResult validate(HttpServletRequest request, String ipAddress) {
        return validate(request.getParameter(validation.getResponseParameter()), ipAddress);
    }

    @Override
    public ValidationResult validate(HttpServletRequest request, String ipAddress, String secretKey) {
        return validate(request.getParameter(validation.getResponseParameter()), ipAddress, secretKey);
    }

    @Override
    public ValidationResult validate(String userResponse, HttpServletRequest request) {
        return validate(userResponse, ipAddressResolver.resolveClientIp(request));
    }

    @Override
    public ValidationResult validate(String userResponse) {
        return validate(userResponse, "");
    }

    @Override
    public ValidationResult validate(String userResponse, String ipAddress) {
        return validate(userResponse, ipAddress, validation.getSecretKey());
    }

    @Override
    public ValidationResult validate(String userResponse, String ipAddress, String secretKey) {
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("secret", secretKey);
        parameters.add("response", userResponse);
        parameters.add("remoteip", ipAddress);

        log.debug("Validating reCAPTCHA:\n    verification url: {}\n    verification parameters: {}", validation.getVerificationUrl(), parameters);

        try {
            ValidationResult result = restTemplate.postForEntity(validation.getVerificationUrl(), parameters, ValidationResult.class).getBody();
            log.debug("reCAPTCHA validation finished: {}", result);
            return result;
        } catch (RestClientException ex) {
            throw new RecaptchaValidationException(userResponse, validation.getVerificationUrl(), ex);
        }
    }
}
