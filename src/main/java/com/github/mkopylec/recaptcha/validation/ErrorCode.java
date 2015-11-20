package com.github.mkopylec.recaptcha.validation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCode {

    MISSING_SECRET_KEY("missing-input-secret"),
    INVALID_SECRET_KEY("invalid-input-secret"),
    MISSING_USER_CAPTCHA_RESPONSE("missing-input-response"),
    INVALID_USER_CAPTCHA_RESPONSE("invalid-input-response");

    private final String text;

    ErrorCode(String text) {
        this.text = text;
    }

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

    @JsonValue
    public String getText() {
        return text;
    }
}
