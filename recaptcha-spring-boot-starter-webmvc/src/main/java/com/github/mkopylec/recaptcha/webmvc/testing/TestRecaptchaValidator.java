package com.github.mkopylec.recaptcha.webmvc.testing;

import com.github.mkopylec.recaptcha.webmvc.RecaptchaProperties;
import com.github.mkopylec.recaptcha.webmvc.validation.ErrorCode;
import com.github.mkopylec.recaptcha.webmvc.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.webmvc.validation.ValidationResult;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public class TestRecaptchaValidator extends RecaptchaValidator {

    protected final RecaptchaProperties.Testing testing;

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

    private ValidationResult getValidationResult() {
        if (testing.isSuccessResult()) {
            return new ValidationResult(true, new ArrayList<ErrorCode>());
        }
        return new ValidationResult(false, testing.getResultErrorCodes());
    }
}
