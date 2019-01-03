package com.github.mkopylec.recaptcha.test.specification

import com.github.mkopylec.recaptcha.commons.RecaptchaProperties
import org.springframework.beans.factory.annotation.Autowired

import static com.github.mkopylec.recaptcha.test.assertions.Assertions.assertThat

class SecuritySpec extends BasicSpec {

    @Autowired
    private RecaptchaProperties recaptcha

    def "Should log in a user and get response data when captcha was not displayed and user entered valid credentials"() {
        //Redirect to login page while trying to get secured data
        when:
        def response = http.getSecuredData()
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("$http.localhostUrl/login")

        //Log in successfully
        when:
        response = http.logIn('user', 'password')
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("$http.localhostUrl/")

        //Get secured data while logged in
        when:
        response = http.getSecuredData()
        then:
        assertThat(response)
                .hasOkStatus()
                .hasMessage(RESPONSE_BODY_MESSAGE)

        cleanup:
        resetUserLoginFailures()
    }

    def "Should log in a user and get response data when captcha was displayed and user entered a valid credentials and captcha response"() {
        given:
        recaptchaServer.stubSuccessfulRecaptchaValidation()
        recaptcha.validation.secretKey = VALID_SECRET

        //Redirect to login page while trying to get secured data
        when:
        def response = http.getSecuredData()
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("$http.localhostUrl/login")

        //Log in unsuccessfully 3 times
        3.times {
            when:
            response = http.logIn('user', 'invalid-password')
            then:
            def query = it == 2 ? '&showRecaptcha' : ''
            assertThat(response)
                    .hasFoundStatus()
                    .redirectsTo("$http.localhostUrl/login?error$query")
        }

        //Log in successfully with captcha
        when:
        response = http.logIn('user', 'password', VALID_CAPTCHA_RESPONSE)
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("$http.localhostUrl/")

        //Get secured data while logged in
        when:
        response = http.getSecuredData()
        then:
        assertThat(response)
                .hasOkStatus()
                .hasMessage(RESPONSE_BODY_MESSAGE)

        cleanup:
        resetUserLoginFailures()
    }

    def "Should not log in a user and not get response data when captcha was displayed and user entered a invalid captcha response"() {
        given:
        recaptchaServer.stubInvalidResponseRecaptchaValidation()
        recaptcha.validation.secretKey = VALID_SECRET

        //Redirect to login page while trying to get secured data
        when:
        def response = http.getSecuredData()
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("$http.localhostUrl/login")

        //Log in unsuccessfully 3 times
        3.times {
            when:
            response = http.logIn('user', 'invalid-password')
            then:
            def query = it == 2 ? '&showRecaptcha' : ''
            assertThat(response)
                    .hasFoundStatus()
                    .redirectsTo("$http.localhostUrl/login?error$query")
        }

        //Log in unsuccessfully because of invalid captcha response
        when:
        response = http.logIn('user', 'password', INVALID_CAPTCHA_RESPONSE)
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("$http.localhostUrl/login?recaptchaError&showRecaptcha")

        //Redirect to login page while trying to get secured data
        when:
        response = http.getSecuredData()
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("$http.localhostUrl/login")

        cleanup:
        resetUserLoginFailures()
    }

    def "Should not log in a user and not get response data when captcha was displayed and user entered invalid credentials"() {
        given:
        recaptchaServer.stubSuccessfulRecaptchaValidation()
        recaptcha.validation.secretKey = VALID_SECRET

        //Redirect to login page while trying to get secured data
        when:
        def response = http.getSecuredData()
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("$http.localhostUrl/login")

        //Log in unsuccessfully 3 times
        3.times {
            when:
            response = http.logIn('user', 'invalid-password')
            then:
            def query = it == 2 ? '&showRecaptcha' : ''
            assertThat(response)
                    .hasFoundStatus()
                    .redirectsTo("$http.localhostUrl/login?error$query")
        }

        //Log in unsuccessfully because of invalid user credentials
        when:
        response = http.logIn('user', 'invalid-password', VALID_CAPTCHA_RESPONSE)
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("$http.localhostUrl/login?error&showRecaptcha")

        //Redirect to login page while trying to get secured data
        when:
        response = http.getSecuredData()
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("$http.localhostUrl/login")

        cleanup:
        resetUserLoginFailures()
    }

    def "Should log in a user and get response data when captcha validation fails because of HTTP error"() {
        given:
        recaptcha.validation.verificationUrl = 'http://invalid.verification.url/'

        //Redirect to login page while trying to get secured data
        when:
        def response = http.getSecuredData()
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("$http.localhostUrl/login")

        //Log in unsuccessfully 3 times
        3.times {
            when:
            response = http.logIn('user', 'invalid-password')
            then:
            def query = it == 2 ? '&showRecaptcha' : ''
            assertThat(response)
                    .hasFoundStatus()
                    .redirectsTo("$http.localhostUrl/login?error$query")
        }

        //Log in successfully with captcha
        when:
        response = http.logIn('user', 'password', VALID_CAPTCHA_RESPONSE)
        then:
        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("$http.localhostUrl/")

        //Get secured data while logged in
        when:
        response = http.getSecuredData()
        then:
        assertThat(response)
                .hasOkStatus()
                .hasMessage(RESPONSE_BODY_MESSAGE)

        cleanup:
        resetUserLoginFailures()
        resetVerificationEndpoint()
    }

    private void resetUserLoginFailures() {
        recaptchaServer.stubSuccessfulRecaptchaValidation()
        recaptcha.validation.secretKey = VALID_SECRET

        def response = http.logIn('user', 'password', VALID_CAPTCHA_RESPONSE)

        assertThat(response)
                .hasFoundStatus()
                .redirectsTo("$http.localhostUrl/")
    }

    private void resetVerificationEndpoint() {
        recaptcha.validation.verificationUrl = 'http://localhost:8081/recaptcha/api/siteverify'
    }
}