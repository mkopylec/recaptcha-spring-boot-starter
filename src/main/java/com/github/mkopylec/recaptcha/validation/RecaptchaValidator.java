package com.github.mkopylec.recaptcha.validation;

import javax.servlet.http.HttpServletRequest;

public interface RecaptchaValidator {

    ValidationResult validate(HttpServletRequest request);

    ValidationResult validate(HttpServletRequest request, String ipAddress);

    ValidationResult validate(HttpServletRequest request, String ipAddress, String secretKey);

    ValidationResult validate(String userResponse, HttpServletRequest request);

    ValidationResult validate(String userResponse);

    ValidationResult validate(String userResponse, String ipAddress);

    ValidationResult validate(String userResponse, String ipAddress, String secretKey);
}
