package com.github.mkopylec.recaptcha.assertions

import com.github.mkopylec.recaptcha.security.ResponseData
import com.github.mkopylec.recaptcha.validation.ErrorCode
import com.github.mkopylec.recaptcha.validation.ValidationResult
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import static org.springframework.http.HttpStatus.FOUND
import static org.springframework.http.HttpStatus.OK

class ResponseAssert {

    private ResponseEntity actual

    protected ResponseAssert(ResponseEntity actual) {
        assert actual != null
        this.actual = actual
    }

    ResponseAssert redirectsTo(String url) {
        assert actual.headers.get('Location') == [url]
        return this
    }

    ResponseAssert hasSuccessfulValidationResult() {
        assert validationResult != null
        assert validationResult.success
        return this
    }

    ResponseAssert hasUnsuccessfulValidationResult() {
        assert validationResult != null
        assert validationResult.failure
        return this
    }

    ResponseAssert hasNoErrorCodes() {
        return hasErrorCodes()
    }

    ResponseAssert hasErrorCodes(ErrorCode... errorCodes) {
        assert validationResult != null
        assert validationResult.errorCodes != null
        assert validationResult.errorCodes.size() == errorCodes.length
        assert validationResult.errorCodes.containsAll(errorCodes)
        return this
    }

    ResponseAssert hasMessage(String message) {
        assert responseData != null
        assert responseData.message == message
        return this
    }

    ResponseAssert hasFoundStatus() {
        return hasStatus(FOUND)
    }

    ResponseAssert hasOkStatus() {
        return hasStatus(OK)
    }

    private ValidationResult getValidationResult() {
        (ValidationResult) actual.body
    }

    private ResponseData getResponseData() {
        (ResponseData) actual.body
    }

    private ResponseAssert hasStatus(HttpStatus status) {
        assert actual.statusCode == status
        return this
    }
}
