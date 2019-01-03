package com.github.mkopylec.recaptcha.test.specification

import com.github.mkopylec.recaptcha.commons.RecaptchaProperties
import org.springframework.beans.factory.annotation.Autowired

import static com.github.mkopylec.recaptcha.commons.validation.ErrorCode.INVALID_SECRET_KEY
import static com.github.mkopylec.recaptcha.commons.validation.ErrorCode.MISSING_USER_CAPTCHA_RESPONSE
import static com.github.mkopylec.recaptcha.test.assertions.Assertions.assertThat

class ValidationSpec extends BasicSpec {

    @Autowired
    private RecaptchaProperties recaptcha

    def "Should successfully validate reCAPTCHA when user response and secret key are valid"() {
        given:
        recaptchaServer.stubSuccessfulRecaptchaValidation()
        recaptcha.validation.secretKey = VALID_SECRET

        when:
        def response = http.validateRecaptcha(VALID_CAPTCHA_RESPONSE)

        then:
        assertThat(response)
                .hasOkStatus()
                .hasSuccessfulValidationResult()
                .hasNoErrorCodes()
    }

    def "Should successfully validate reCAPTCHA with custom ip address when user response and secret key are valid"() {
        given:
        recaptchaServer.stubCustomIpSuccessfulRecaptchaValidation()
        recaptcha.validation.secretKey = VALID_SECRET

        when:
        def response = http.validateRecaptchaWithIp(VALID_CAPTCHA_RESPONSE)

        then:
        assertThat(response)
                .hasOkStatus()
                .hasSuccessfulValidationResult()
                .hasNoErrorCodes()
    }

    def "Should unsuccessfully validate reCAPTCHA when secret key is invalid"() {
        given:
        recaptchaServer.stubInvalidSecretRecaptchaValidation()
        recaptcha.validation.secretKey = INVALID_SECRET

        when:
        def response = http.validateRecaptcha(VALID_CAPTCHA_RESPONSE)

        then:
        assertThat(response)
                .hasOkStatus()
                .hasUnsuccessfulValidationResult()
                .hasErrorCodes(INVALID_SECRET_KEY)
    }

    def "Should unsuccessfully validate reCAPTCHA when user response is missing"() {
        given:
        recaptchaServer.stubMissingResponseRecaptchaValidation()
        recaptcha.validation.secretKey = VALID_SECRET

        when:
        def response = http.validateRecaptcha(null)

        then:
        assertThat(response)
                .hasOkStatus()
                .hasUnsuccessfulValidationResult()
                .hasErrorCodes(MISSING_USER_CAPTCHA_RESPONSE)
    }
}