package com.github.mkopylec.recaptcha.webmvc.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseData {

    private final String message;

    @JsonCreator
    public ResponseData(@JsonProperty("message") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
