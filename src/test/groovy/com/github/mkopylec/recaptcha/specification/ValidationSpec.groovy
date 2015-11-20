package com.github.mkopylec.recaptcha.specification

import com.github.mkopylec.recaptcha.BasicSpec
import com.github.mkopylec.recaptcha.RecaptchaProperties
import com.github.mkopylec.recaptcha.validation.ValidationResult
import org.springframework.beans.factory.annotation.Autowired

import static com.github.mkopylec.recaptcha.Strings.INVALID_SECRET
import static com.github.mkopylec.recaptcha.Strings.VALID_CAPTCHA_RESPONSE
import static com.github.mkopylec.recaptcha.Strings.VALID_SECRET
import static com.github.mkopylec.recaptcha.stubs.RecaptchaValidationStubs.stubCustomIpSuccessfulRecaptchaValidation
import static com.github.mkopylec.recaptcha.stubs.RecaptchaValidationStubs.stubInvalidSecretRecaptchaValidation
import static com.github.mkopylec.recaptcha.stubs.RecaptchaValidationStubs.stubMissingResponseRecaptchaValidation
import static com.github.mkopylec.recaptcha.stubs.RecaptchaValidationStubs.stubSuccessfulRecaptchaValidation
import static com.github.mkopylec.recaptcha.validation.ErrorCode.INVALID_SECRET_KEY
import static com.github.mkopylec.recaptcha.validation.ErrorCode.MISSING_USER_CAPTCHA_RESPONSE
import static org.springframework.http.HttpStatus.OK

class ValidationSpec extends BasicSpec {

    @Autowired
    private RecaptchaProperties recaptcha

    def "Should successfully validate reCAPTCHA when user response and secret key are valid"() {
        given:
        stubSuccessfulRecaptchaValidation()
        recaptcha.validation.secretKey = VALID_SECRET

        when:
        def response = POST '/testValidation/userResponse', ValidationResult, ['g-recaptcha-response': VALID_CAPTCHA_RESPONSE]

        then:
        response.statusCode == OK
        response.body.success
        response.body.errorCodes == []
    }

    def "Should successfully validate reCAPTCHA with custom ip address when user response and secret key are valid"() {
        given:
        stubCustomIpSuccessfulRecaptchaValidation()
        recaptcha.validation.secretKey = VALID_SECRET

        when:
        def response = POST 'testValidation/userResponseAndIp', ValidationResult, ['g-recaptcha-response': VALID_CAPTCHA_RESPONSE]

        then:
        response.statusCode == OK
        response.body.success
        response.body.errorCodes == []
    }

    def "Should unsuccessfully validate reCAPTCHA when secret key is invalid"() {
        given:
        stubInvalidSecretRecaptchaValidation()
        recaptcha.validation.secretKey = INVALID_SECRET

        when:
        def response = POST 'testValidation/userResponse', ValidationResult, ['g-recaptcha-response': VALID_CAPTCHA_RESPONSE]

        then:
        response.statusCode == OK
        !response.body.success
        response.body.errorCodes == [INVALID_SECRET_KEY]
    }

    def "Should unsuccessfully validate reCAPTCHA when user response is missing"() {
        given:
        stubMissingResponseRecaptchaValidation()
        recaptcha.validation.secretKey = VALID_SECRET

        when:
        def response = POST 'testValidation/userResponse', ValidationResult

        then:
        response.statusCode == OK
        !response.body.success
        response.body.errorCodes == [MISSING_USER_CAPTCHA_RESPONSE]
    }
}