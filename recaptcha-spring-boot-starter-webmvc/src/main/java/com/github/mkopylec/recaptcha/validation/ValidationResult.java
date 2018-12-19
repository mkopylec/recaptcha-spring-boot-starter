package com.github.mkopylec.recaptcha.validation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class ValidationResult {

    private boolean success;
    private List<ErrorCode> errorCodes;

    @JsonCreator
    public ValidationResult(
            @JsonProperty("success") boolean success,
            @JsonProperty("error-codes") List<ErrorCode> errorCodes
    ) {
        this.success = success;
        this.errorCodes = errorCodes == null ? new ArrayList<>() : errorCodes;
    }

    public boolean isSuccess() {
        return success;
    }

    @JsonIgnore
    public boolean isFailure() {
        return !success;
    }

    public List<ErrorCode> getErrorCodes() {
        return unmodifiableList(errorCodes);
    }

    public boolean hasError(ErrorCode error) {
        return errorCodes.contains(error);
    }

    @Override
    public String toString() {
        return "ValidationResult{" +
                "success=" + success +
                ", errorCodes=" + errorCodes +
                '}';
    }
}
