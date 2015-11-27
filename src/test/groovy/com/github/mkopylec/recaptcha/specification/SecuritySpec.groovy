package com.github.mkopylec.recaptcha.specification

import com.github.mkopylec.recaptcha.BasicSpec
import com.github.mkopylec.recaptcha.RecaptchaProperties
import com.github.mkopylec.recaptcha.security.ResponseData
import org.springframework.beans.factory.annotation.Autowired

import static com.github.mkopylec.recaptcha.Strings.INVALID_CAPTCHA_RESPONSE
import static com.github.mkopylec.recaptcha.Strings.RESPONSE_DATA_MESSAGE
import static com.github.mkopylec.recaptcha.Strings.VALID_CAPTCHA_RESPONSE
import static com.github.mkopylec.recaptcha.Strings.VALID_SECRET
import static com.github.mkopylec.recaptcha.stubs.RecaptchaValidationStubs.stubInvalidResponseRecaptchaValidation
import static com.github.mkopylec.recaptcha.stubs.RecaptchaValidationStubs.stubSuccessfulRecaptchaValidation
import static org.springframework.http.HttpStatus.FOUND
import static org.springframework.http.HttpStatus.OK

class SecuritySpec extends BasicSpec {

    @Autowired
    private RecaptchaProperties recaptcha

    def "Should log in a user and get response data when captcha was not displayed and user entered valid credentials"() {
        //Redirect to login page while trying to get secured data
        when:
        def response = POST 'testSecurity/getResponse', Object
        then:
        response.statusCode == FOUND
        response.headers.get('Location') == ['http://localhost:' + port + '/login']

        //Log in successfully
        when:
        response = POST 'login', Object, ['username': 'user', 'password': 'password']
        then:
        response.statusCode == FOUND
        response.headers.get('Location') == ['http://localhost:' + port + '/']

        //Get secured while logged in
        when:
        response = POST 'testSecurity/getResponse', ResponseData
        then:
        response.statusCode == OK
        response.body.message == RESPONSE_DATA_MESSAGE

        resetUserLoginFailures()
    }

    def "Should log in a user and get response data when captcha was displayed and user entered a valid credentials and captcha response"() {
        given:
        stubSuccessfulRecaptchaValidation()
        recaptcha.validation.secretKey = VALID_SECRET

        //Redirect to login page while trying to get secured data
        when:
        def response = POST 'testSecurity/getResponse', Object
        then:
        response.statusCode == FOUND
        response.headers.get('Location') == ['http://localhost:' + port + '/login']

        //Log in unsuccessfully 3 times
        3.times {
            when:
            response = POST 'login', Object, ['username': 'user', 'password': 'invalid-password']
            then:
            response.statusCode == FOUND
            response.headers.get('Location') == ['http://localhost:' + port + '/' + (it == 3 ? '?showRecaptcha' : '')]
        }

        //Log in successfully with captcha
        when:
        response = POST 'login', Object, ['username': 'user', 'password': 'password', 'g-recaptcha-response': VALID_CAPTCHA_RESPONSE]
        then:
        response.statusCode == FOUND
        response.headers.get('Location') == ['http://localhost:' + port + '/']

        //Get secured while logged in
        when:
        response = POST 'testSecurity/getResponse', ResponseData
        then:
        response.statusCode == OK
        response.body.message == RESPONSE_DATA_MESSAGE

        resetUserLoginFailures()
    }

    def "Should not log in a user and not get response data when captcha was displayed and user entered a invalid captcha response"() {
        given:
        stubInvalidResponseRecaptchaValidation()
        recaptcha.validation.secretKey = VALID_SECRET

        //Redirect to login page while trying to get secured data
        when:
        def response = POST 'testSecurity/getResponse', Object
        then:
        response.statusCode == FOUND
        response.headers.get('Location') == ['http://localhost:' + port + '/login']

        //Log in unsuccessfully 3 times
        3.times {
            when:
            response = POST 'login', Object, ['username': 'user', 'password': 'invalid-password']
            then:
            response.statusCode == FOUND
            response.headers.get('Location') == ['http://localhost:' + port + '/login' + (it == 3 ? '?showRecaptcha' : '')]
        }

        //Log in unsuccessfully because of invalid captcha response
        when:
        response = POST 'login', Object, ['username': 'user', 'password': 'password', 'g-recaptcha-response': INVALID_CAPTCHA_RESPONSE]
        then:
        response.statusCode == FOUND
        response.headers.get('Location') == ['http://localhost:' + port + '/login?recaptchaError&showRecaptcha']

        //Redirect to login page while trying to get secured data
        when:
        response = POST 'testSecurity/getResponse', ResponseData
        then:
        response.statusCode == FOUND
        response.headers.get('Location') == ['http://localhost:' + port + '/login']

        resetUserLoginFailures()
    }

    def "Should not log in a user and not get response data when captcha was displayed and user entered invalid credentials"() {
        given:
        stubSuccessfulRecaptchaValidation()
        recaptcha.validation.secretKey = VALID_SECRET

        //Redirect to login page while trying to get secured data
        when:
        def response = POST 'testSecurity/getResponse', Object
        then:
        response.statusCode == FOUND
        response.headers.get('Location') == ['http://localhost:' + port + '/login']

        //Log in unsuccessfully 3 times
        3.times {
            when:
            response = POST 'login', Object, ['username': 'user', 'password': 'invalid-password']
            then:
            response.statusCode == FOUND
            response.headers.get('Location') == ['http://localhost:' + port + '/login' + (it == 3 ? '?showRecaptcha' : '')]
        }

        //Log in unsuccessfully because of invalid user credentials
        when:
        response = POST 'login', Object, ['username': 'user', 'password': 'invalid-password', 'g-recaptcha-response': VALID_CAPTCHA_RESPONSE]
        then:
        response.statusCode == FOUND
        response.headers.get('Location') == ['http://localhost:' + port + '/login?error&showRecaptcha']

        //Redirect to login page while trying to get secured data
        when:
        response = POST 'testSecurity/getResponse', ResponseData
        then:
        response.statusCode == FOUND
        response.headers.get('Location') == ['http://localhost:' + port + '/login']

        resetUserLoginFailures()
    }

    def "Should log in a user and get response data when captcha validation fails because of HTTP error"() {
        given:
        recaptcha.validation.verificationUrl = 'http://invalid.verification.url/'

        //Redirect to login page while trying to get secured data
        when:
        def response = POST 'testSecurity/getResponse', Object
        then:
        response.statusCode == FOUND
        response.headers.get('Location') == ['http://localhost:' + port + '/login']

        //Log in unsuccessfully 3 times
        3.times {
            when:
            response = POST 'login', Object, ['username': 'user', 'password': 'invalid-password']
            then:
            response.statusCode == FOUND
            response.headers.get('Location') == ['http://localhost:' + port + '/login' + (it == 3 ? '?showRecaptcha' : '')]
        }

        //Log in successfully with captcha
        when:
        response = POST 'login', Object, ['username': 'user', 'password': 'password', 'g-recaptcha-response': VALID_CAPTCHA_RESPONSE]
        then:
        response.statusCode == FOUND
        response.headers.get('Location') == ['http://localhost:' + port + '/']

        //Get secured while logged in
        when:
        response = POST 'testSecurity/getResponse', ResponseData
        then:
        response.statusCode == OK
        response.body.message == RESPONSE_DATA_MESSAGE

        resetUserLoginFailures()
        resetVerificationEndpoint()
    }

    private void resetUserLoginFailures() {
        stubSuccessfulRecaptchaValidation()
        recaptcha.validation.secretKey = VALID_SECRET

        def response = POST 'login', Object, ['username': 'user', 'password': 'password', 'g-recaptcha-response': VALID_CAPTCHA_RESPONSE]

        assert response.statusCode == FOUND
        assert response.headers.get('Location') == ['http://localhost:' + port + '/']
    }

    private void resetVerificationEndpoint() {
        recaptcha.validation.verificationUrl = 'http://localhost:8081/recaptcha/api/siteverify'
    }
}