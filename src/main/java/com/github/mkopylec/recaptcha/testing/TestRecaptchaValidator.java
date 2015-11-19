package com.github.mkopylec.recaptcha.testing;

import com.github.mkopylec.recaptcha.RecaptchaProperties.Testing;
import com.github.mkopylec.recaptcha.validation.ErrorCode;
import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.validation.ValidationResult;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import static com.github.mkopylec.recaptcha.validation.ErrorCode.INVALID_USER_CAPTCHA_RESPONSE;
import static java.util.Collections.singletonList;

public class TestRecaptchaValidator extends RecaptchaValidator {

    protected final Testing testing;

    public TestRecaptchaValidator(Testing testing) {
        super(null, null);
        this.testing = testing;
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
    public ValidationResult validate(String userResponse) {
        return getValidationResult();
    }

    @Override
    public ValidationResult validate(String userResponse, String ipAddress) {
        return getValidationResult();
    }

    private ValidationResult getValidationResult() {
        if (testing.isSuccessResult()) {
            return new ValidationResult(true, new ArrayList<ErrorCode>());
        }
        return new ValidationResult(false, singletonList(INVALID_USER_CAPTCHA_RESPONSE));
    }
}
