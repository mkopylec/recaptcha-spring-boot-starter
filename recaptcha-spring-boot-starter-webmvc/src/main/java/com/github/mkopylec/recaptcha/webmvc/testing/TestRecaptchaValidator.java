package com.github.mkopylec.recaptcha.webmvc.testing;

import com.github.mkopylec.recaptcha.commons.RecaptchaProperties;
import com.github.mkopylec.recaptcha.commons.RecaptchaProperties.Testing;
import com.github.mkopylec.recaptcha.commons.validation.ValidationResult;
import com.github.mkopylec.recaptcha.webmvc.validation.RecaptchaValidator;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import static org.slf4j.LoggerFactory.getLogger;

public class TestRecaptchaValidator extends RecaptchaValidator {

    private static final Logger log = getLogger(TestRecaptchaValidator.class);

    protected final Testing testing;

    public TestRecaptchaValidator(RecaptchaProperties recaptcha) {
        super(null, recaptcha);
        testing = recaptcha.getTesting();
    }

    @Override
    public ValidationResult validate(HttpServletRequest request) {
        return getValidationResult();
    }

    @Override
    public ValidationResult validate(HttpServletRequest request, String ipAddress) {
        return getValidationResult();
    }

    @Override
    public ValidationResult validate(HttpServletRequest request, String ipAddress, String secretKey) {
        return getValidationResult();
    }

    @Override
    public ValidationResult validate(String userResponse) {
        return getValidationResult();
    }

    @Override
    public ValidationResult validate(String userResponse, String ipAddress) {
        return getValidationResult();
    }

    @Override
    public ValidationResult validate(String userResponse, String ipAddress, String secretKey) {
        return getValidationResult();
    }

    protected ValidationResult getValidationResult() {
        ValidationResult result = new ValidationResult(false, testing.getResultErrorCodes());
        if (testing.isSuccessResult()) {
            result = new ValidationResult(true, new ArrayList<>());
        }
        log.debug("reCAPTCHA validation finished: {}", result);
        return result;
    }
}
