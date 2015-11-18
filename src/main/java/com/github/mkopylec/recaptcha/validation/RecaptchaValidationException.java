package com.github.mkopylec.recaptcha.validation;

import static java.lang.String.format;

public class RecaptchaValidationException extends RuntimeException {

    public RecaptchaValidationException(String userResponse, String verificationUrl, Throwable cause) {
        super(format("Error validating reCAPTCHA. User response: '%s', verification URL: '%s'", userResponse, verificationUrl), cause);
    }
}
