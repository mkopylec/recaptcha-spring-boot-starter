package com.github.mkopylec.recaptcha.test.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseBody {

    private final String message;

    @JsonCreator
    public ResponseBody(@JsonProperty("message") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
