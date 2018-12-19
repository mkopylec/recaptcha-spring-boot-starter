package com.github.mkopylec.recaptcha.specification

import com.github.mkopylec.recaptcha.BasicSpec
import com.github.mkopylec.recaptcha.RecaptchaProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestPropertySource

import static com.github.mkopylec.recaptcha.assertions.Assertions.assertThat
import static com.github.mkopylec.recaptcha.validation.ErrorCode.INVALID_SECRET_KEY
import static com.github.mkopylec.recaptcha.validation.ErrorCode.INVALID_USER_CAPTCHA_RESPONSE
import static com.github.mkopylec.recaptcha.validation.ErrorCode.MISSING_USER_CAPTCHA_RESPONSE

@TestPropertySource(properties = ['recaptcha.testing.enabled: true'])
class TestingSpec extends BasicSpec {

    @Autowired
    private RecaptchaProperties recaptcha

    def "Should successfully validate reCAPTCHA when result is set to success"() {
        given:
        recaptcha.testing.successResult = true

        when:
        def response = validateRecaptchaInTestingMode()

        then:
        assertThat(response)
                .hasOkStatus()
                .hasSuccessfulValidationResult()
                .hasNoErrorCodes()

        cleanup:
        resetRecaptchaProperties()
    }

    def "Should unsuccessfully validate reCAPTCHA when result is set to failure"() {
        given:
        recaptcha.testing.successResult = false
        recaptcha.testing.resultErrorCodes = [MISSING_USER_CAPTCHA_RESPONSE]

        when:
        def response = validateRecaptchaInTestingMode()

        then:
        assertThat(response)
                .hasOkStatus()
                .hasUnsuccessfulValidationResult()
                .hasErrorCodes(MISSING_USER_CAPTCHA_RESPONSE)

        cleanup:
        resetRecaptchaProperties()
    }

    def "Should unsuccessfully validate reCAPTCHA with specific error codes when result is set to failure"() {
        given:
        recaptcha.testing.successResult = false
        recaptcha.testing.resultErrorCodes = [INVALID_SECRET_KEY, INVALID_USER_CAPTCHA_RESPONSE]

        when:
        def response = validateRecaptchaInTestingMode()

        then:
        assertThat(response)
                .hasOkStatus()
                .hasUnsuccessfulValidationResult()
                .hasErrorCodes(INVALID_SECRET_KEY, INVALID_USER_CAPTCHA_RESPONSE)

        cleanup:
        resetRecaptchaProperties()
    }

    private void resetRecaptchaProperties() {
        recaptcha.testing.resultErrorCodes = []
        recaptcha.testing.successResult = true
    }
}