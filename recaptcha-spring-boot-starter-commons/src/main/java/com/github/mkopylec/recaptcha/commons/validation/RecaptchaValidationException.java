package com.github.mkopylec.recaptcha.commons.validation;

public class RecaptchaValidationException extends RuntimeException {

    public RecaptchaValidationException(String verificationUrl, Throwable cause) {
        super("Error validating reCAPTCHA. Verification URL: " + verificationUrl, cause);
    }
}
