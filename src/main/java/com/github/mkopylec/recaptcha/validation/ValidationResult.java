package com.github.mkopylec.recaptcha.validation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

    private boolean success;

    @JsonProperty("error-codes")
    private List<ErrorCode> errorCodes = new ArrayList<>();

    public boolean isSuccess() {
        return success;
    }

    protected void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ErrorCode> getErrorCodes() {
        return errorCodes;
    }

    protected void setErrorCodes(List<ErrorCode> errorCodes) {
        this.errorCodes = errorCodes;
    }

    @Override
    public String toString() {
        return "ValidationResult{" +
                "success=" + success +
                ", errorCodes=" + errorCodes +
                '}';
    }
}
