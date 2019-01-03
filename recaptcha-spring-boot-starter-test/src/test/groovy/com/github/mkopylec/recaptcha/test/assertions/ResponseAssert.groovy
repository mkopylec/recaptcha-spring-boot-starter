package com.github.mkopylec.recaptcha.test.assertions

import com.github.mkopylec.recaptcha.commons.validation.ErrorCode
import com.github.mkopylec.recaptcha.commons.validation.ValidationResult
import com.github.mkopylec.recaptcha.test.utils.HttpResponse
import com.github.mkopylec.recaptcha.test.utils.ResponseBody

class ResponseAssert {

    private HttpResponse actual

    protected ResponseAssert(HttpResponse actual) {
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
        assert responseBody != null
        assert responseBody.message == message
        return this
    }

    ResponseAssert hasFoundStatus() {
        return hasStatus(302)
    }

    ResponseAssert hasOkStatus() {
        return hasStatus(200)
    }

    private ValidationResult getValidationResult() {
        (ValidationResult) actual.body
    }

    private ResponseBody getResponseBody() {
        (ResponseBody) actual.body
    }

    private ResponseAssert hasStatus(int status) {
        assert actual.status == status
        return this
    }
}
