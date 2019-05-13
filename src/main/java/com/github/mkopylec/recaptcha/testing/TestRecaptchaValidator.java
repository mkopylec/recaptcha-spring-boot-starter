package com.github.mkopylec.recaptcha.testing;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import com.github.mkopylec.recaptcha.RecaptchaProperties.Testing;
import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.validation.ValidationResult;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public class TestRecaptchaValidator extends RecaptchaValidator {

    protected final Testing testing;

    public TestRecaptchaValidator(RecaptchaProperties recaptcha) {
        super(null, recaptcha, null);
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

    private ValidationResult getValidationResult() {
        if (testing.isSuccessResult()) {
            return new ValidationResult(true, new ArrayList<>());
        }
        return new ValidationResult(false, testing.getResultErrorCodes());
    }
}
