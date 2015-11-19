package com.github.mkopylec.recaptcha.validation;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ErrorCode {

    MISSING_SECRET_KEY,
    INVALID_SECRET_KEY,
    MISSING_USER_CAPTCHA_RESPONSE,
    INVALID_USER_CAPTCHA_RESPONSE;

    @JsonCreator
    private static ErrorCode fromValue(String value) {
        if (value == null) {
            return null;
        }
        switch (value) {
            case "missing-input-secret":
                return MISSING_SECRET_KEY;
            case "invalid-input-secret":
                return INVALID_SECRET_KEY;
            case "missing-input-response":
                return MISSING_USER_CAPTCHA_RESPONSE;
            case "invalid-input-response":
                return INVALID_USER_CAPTCHA_RESPONSE;
            default:
                throw new IllegalArgumentException("Invalid error code");
        }
    }
}
