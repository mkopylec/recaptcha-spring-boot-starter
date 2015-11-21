package com.github.mkopylec.recaptcha.specification

import com.github.mkopylec.recaptcha.BasicSpec
import com.github.mkopylec.recaptcha.RecaptchaProperties
import com.github.mkopylec.recaptcha.validation.ValidationResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestPropertySource

import static com.github.mkopylec.recaptcha.validation.ErrorCode.INVALID_SECRET_KEY
import static com.github.mkopylec.recaptcha.validation.ErrorCode.INVALID_USER_CAPTCHA_RESPONSE
import static com.github.mkopylec.recaptcha.validation.ErrorCode.MISSING_USER_CAPTCHA_RESPONSE
import static org.springframework.http.HttpStatus.OK

@TestPropertySource(properties = ['recaptcha.testing.enabled: true'])
class TestingSpec extends BasicSpec {

    @Autowired
    private RecaptchaProperties recaptcha

    def "Should successfully validate reCAPTCHA when result is set to success"() {
        given:
        recaptcha.testing.successResult = true

        when:
        def response = POST '/testTesting/validate', ValidationResult

        then:
        response.statusCode == OK
        response.body.success
        response.body.errorCodes == []

        resetRecaptchaProperties()
    }

    def "Should unsuccessfully validate reCAPTCHA when result is set to failure"() {
        given:
        recaptcha.testing.successResult = false
        recaptcha.testing.resultErrorCodes = [MISSING_USER_CAPTCHA_RESPONSE]

        when:
        def response = POST '/testTesting/validate', ValidationResult

        then:
        response.statusCode == OK
        !response.body.success
        response.body.errorCodes == [MISSING_USER_CAPTCHA_RESPONSE]

        resetRecaptchaProperties()
    }

    def "Should unsuccessfully validate reCAPTCHA with specific error codes when result is set to failure"() {
        given:
        recaptcha.testing.successResult = false
        recaptcha.testing.resultErrorCodes = [INVALID_SECRET_KEY, INVALID_USER_CAPTCHA_RESPONSE]

        when:
        def response = POST '/testTesting/validate', ValidationResult

        then:
        response.statusCode == OK
        !response.body.success
        response.body.errorCodes == [INVALID_SECRET_KEY, INVALID_USER_CAPTCHA_RESPONSE]

        resetRecaptchaProperties()
    }

    private void resetRecaptchaProperties() {
        recaptcha.testing.resultErrorCodes = []
        recaptcha.testing.successResult = true
    }
}