package com.github.mkopylec.recaptcha.specification

import com.github.mkopylec.recaptcha.BasicSpec
import com.github.mkopylec.recaptcha.RecaptchaProperties
import com.github.mkopylec.recaptcha.security.ResponseData
import org.springframework.beans.factory.annotation.Autowired

import static com.github.mkopylec.recaptcha.Strings.RESPONSE_DATA_MESSAGE
import static com.github.mkopylec.recaptcha.Strings.VALID_CAPTCHA_RESPONSE
import static com.github.mkopylec.recaptcha.Strings.VALID_SECRET
import static com.github.mkopylec.recaptcha.stubs.RecaptchaValidationStubs.stubMissingResponseRecaptchaValidation
import static com.github.mkopylec.recaptcha.stubs.RecaptchaValidationStubs.stubSuccessfulRecaptchaValidation
import static org.springframework.http.HttpStatus.FOUND
import static org.springframework.http.HttpStatus.OK

class SecuritySpec extends BasicSpec {

    @Autowired
    private RecaptchaProperties recaptcha

    def "Should get response data after logging in when captcha is valid"() {
        given:
        stubSuccessfulRecaptchaValidation()
        recaptcha.validation.secretKey = VALID_SECRET

        when:
        def response = POST 'testSecurity/getResponse', Object
        then:
        response.statusCode == FOUND
        response.headers.get('Location') == ['http://localhost:' + port + '/login']

        when:
        response = POST 'login', Object, ['username': 'user', 'password': 'password', 'g-recaptcha-response': VALID_CAPTCHA_RESPONSE]
        then:
        response.statusCode == FOUND
        response.headers.get('Location') == ['http://localhost:' + port + '/']

        when:
        response = POST 'testSecurity/getResponse', ResponseData
        then:
        response.statusCode == OK
        response.body.message == RESPONSE_DATA_MESSAGE
    }

    def "Should not get response data after logging in when captcha is invalid"() {
        given:
        stubMissingResponseRecaptchaValidation()
        recaptcha.validation.secretKey = VALID_SECRET

        when:
        def response = POST 'testSecurity/getResponse', Object
        then:
        response.statusCode == FOUND
        response.headers.get('Location') == ['http://localhost:' + port + '/login']

        when:
        response = POST 'login', Object, ['username': 'user', 'password': 'password']
        then:
        response.statusCode == FOUND
        response.headers.get('Location') == ['http://localhost:' + port + '/login?recaptchaError']

        when:
        response = POST 'testSecurity/getResponse', ResponseData
        then:
        response.statusCode == FOUND
        response.headers.get('Location') == ['http://localhost:' + port + '/login']
    }
}