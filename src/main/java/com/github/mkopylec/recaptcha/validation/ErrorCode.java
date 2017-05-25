package com.github.mkopylec.recaptcha.validation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public enum ErrorCode {

    //reCAPTCHA verification errors
    MISSING_SECRET_KEY("missing-input-secret"),
    INVALID_SECRET_KEY("invalid-input-secret"),
    MISSING_USER_CAPTCHA_RESPONSE("missing-input-response"),
    INVALID_USER_CAPTCHA_RESPONSE("invalid-input-response"),
    BAD_REQUEST("bad-request"),
    TIMEOUT_OR_DUPLICATE("timeout-or-duplicate"),

    //Custom errors
    MISSING_USERNAME_REQUEST_PARAMETER("missing-username-request-parameter"),
    MISSING_CAPTCHA_RESPONSE_PARAMETER("missing-captcha-response-parameter"),
    VALIDATION_HTTP_ERROR("validation-http-error"),
    UNSUPPORTED_ERROR_CODE("unsupported_error_code");

    private static final Logger log = getLogger(ErrorCode.class);

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
            case "bad-request":
                return BAD_REQUEST;
            case "timeout-or-duplicate":
                return TIMEOUT_OR_DUPLICATE;
            case "missing-username-request-parameter":
                return MISSING_USERNAME_REQUEST_PARAMETER;
            case "missing-captcha-response-parameter":
                return MISSING_CAPTCHA_RESPONSE_PARAMETER;
            case "validation-http-error":
                return VALIDATION_HTTP_ERROR;
            default:
                log.warn("Unsupported error code: {}", value);
                return UNSUPPORTED_ERROR_CODE;
        }
    }

    @JsonValue
    public String getText() {
        return text;
    }
}
