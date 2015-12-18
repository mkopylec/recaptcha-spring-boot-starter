package com.github.mkopylec.recaptcha.specification

import com.github.mkopylec.recaptcha.BasicSpec
import com.github.mkopylec.recaptcha.RecaptchaProperties
import org.springframework.beans.factory.annotation.Autowired

import static com.github.mkopylec.recaptcha.Strings.INVALID_CAPTCHA_RESPONSE
import static com.github.mkopylec.recaptcha.Strings.RESPONSE_DATA_MESSAGE
import static com.github.mkopylec.recaptcha.Strings.VALID_CAPTCHA_RESPONSE
import static com.github.mkopylec.recaptcha.Strings.VALID_SECRET
import static com.github.mkopylec.recaptcha.assertions.Assertions.assertThat
import static com.github.mkopylec.recaptcha.stubs.RecaptchaValidationStubs.stubInvalidResponseRecaptchaValidation
import static com.github.mkopylec.recaptcha.stubs.RecaptchaValidationStubs.stubSuccessfulRecaptchaValidation

class SecuritySpec extends BasicSpec {

    @Autowired
    private RecaptchaProperties recaptcha

    def "Should log in a user and get response data when captcha was not displayed and user entered valid credentials"() {
        //Redirect to login page while trying to get secured data
        when:
        def response = getSecuredData()
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("http://localhost:$port/login")

        //Log in successfully
        when:
        response = logIn('user', 'password')
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("http://localhost:$port/")

        //Get secured data while logged in
        when:
        response = getSecuredData()
        then:
        assertThat(response)
                .hasOkStatus()
                .hasMessage(RESPONSE_DATA_MESSAGE)

        cleanup:
        resetUserLoginFailures()
    }

    def "Should log in a user and get response data when captcha was displayed and user entered a valid credentials and captcha response"() {
        given:
        stubSuccessfulRecaptchaValidation()
        recaptcha.validation.secretKey = VALID_SECRET

        //Redirect to login page while trying to get secured data
        when:
        def response = getSecuredData()
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("http://localhost:$port/login")

        //Log in unsuccessfully 3 times
        3.times {
            when:
            response = logIn('user', 'invalid-password')
            then:
            def query = it == 2 ? '&showRecaptcha' : ''
            assertThat(response)
                    .hasFoundStatus()
                    .redirectsTo("http://localhost:$port/login?error$query")
        }

        //Log in successfully with captcha
        when:
        response = logIn('user', 'password', VALID_CAPTCHA_RESPONSE)
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("http://localhost:$port/")

        //Get secured data while logged in
        when:
        response = getSecuredData()
        then:
        assertThat(response)
                .hasOkStatus()
                .hasMessage(RESPONSE_DATA_MESSAGE)

        cleanup:
        resetUserLoginFailures()
    }

    def "Should not log in a user and not get response data when captcha was displayed and user entered a invalid captcha response"() {
        given:
        stubInvalidResponseRecaptchaValidation()
        recaptcha.validation.secretKey = VALID_SECRET

        //Redirect to login page while trying to get secured data
        when:
        def response = getSecuredData()
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("http://localhost:$port/login")

        //Log in unsuccessfully 3 times
        3.times {
            when:
            response = logIn('user', 'invalid-password')
            then:
            def query = it == 2 ? '&showRecaptcha' : ''
            assertThat(response)
                    .hasFoundStatus()
                    .redirectsTo("http://localhost:$port/login?error$query")
        }

        //Log in unsuccessfully because of invalid captcha response
        when:
        response = logIn('user', 'password', INVALID_CAPTCHA_RESPONSE)
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("http://localhost:$port/login?recaptchaError&showRecaptcha")

        //Redirect to login page while trying to get secured data
        when:
        response = getSecuredData()
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("http://localhost:$port/login")

        cleanup:
        resetUserLoginFailures()
    }

    def "Should not log in a user and not get response data when captcha was displayed and user entered invalid credentials"() {
        given:
        stubSuccessfulRecaptchaValidation()
        recaptcha.validation.secretKey = VALID_SECRET

        //Redirect to login page while trying to get secured data
        when:
        def response = getSecuredData()
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("http://localhost:$port/login")

        //Log in unsuccessfully 3 times
        3.times {
            when:
            response = logIn('user', 'invalid-password')
            then:
            def query = it == 2 ? '&showRecaptcha' : ''
            assertThat(response)
                    .hasFoundStatus()
                    .redirectsTo("http://localhost:$port/login?error$query")
        }

        //Log in unsuccessfully because of invalid user credentials
        when:
        response = logIn('user', 'invalid-password', VALID_CAPTCHA_RESPONSE)
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("http://localhost:$port/login?error&showRecaptcha")

        //Redirect to login page while trying to get secured data
        when:
        response = getSecuredData()
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("http://localhost:$port/login")

        cleanup:
        resetUserLoginFailures()
    }

    def "Should log in a user and get response data when captcha validation fails because of HTTP error"() {
        given:
        recaptcha.validation.verificationUrl = 'http://invalid.verification.url/'

        //Redirect to login page while trying to get secured data
        when:
        def response = getSecuredData()
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("http://localhost:$port/login")

        //Log in unsuccessfully 3 times
        3.times {
            when:
            response = logIn('user', 'invalid-password')
            then:
            def query = it == 2 ? '&showRecaptcha' : ''
            assertThat(response)
                    .hasFoundStatus()
                    .redirectsTo("http://localhost:$port/login?error$query")
        }

        //Log in successfully with captcha
        when:
        response = logIn('user', 'password', VALID_CAPTCHA_RESPONSE)
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("http://localhost:$port/")

        //Get secured data while logged in
        when:
        response = getSecuredData()
        then:
        assertThat(response)
                .hasOkStatus()
                .hasMessage(RESPONSE_DATA_MESSAGE)

        cleanup:
        resetUserLoginFailures()
        resetVerificationEndpoint()
    }

    private void resetUserLoginFailures() {
        stubSuccessfulRecaptchaValidation()
        recaptcha.validation.secretKey = VALID_SECRET

        def response = logIn('user', 'password', VALID_CAPTCHA_RESPONSE)

        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("http://localhost:$port/")
    }

    private void resetVerificationEndpoint() {
        recaptcha.validation.verificationUrl = 'http://localhost:8081/recaptcha/api/siteverify'
    }
}