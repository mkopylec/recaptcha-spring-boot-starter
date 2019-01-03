package com.github.mkopylec.recaptcha.test.specification

import com.github.mkopylec.recaptcha.test.stubs.RecaptchaServerMock
import com.github.mkopylec.recaptcha.test.utils.HttpClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
abstract class BasicSpec extends Specification {

    public static final String REMOTE_IP_ADDRESS = "69.007.100.200";
    public static final String VALID_CAPTCHA_RESPONSE = "valid-captcha";
    public static final String INVALID_CAPTCHA_RESPONSE = "invalid-captcha";
    public static final String VALID_SECRET = "valid-secret";
    public static final String INVALID_SECRET = "invalid-secret";
    public static final String RESPONSE_BODY_MESSAGE = "I am response";

    protected static RecaptchaServerMock recaptchaServer = new RecaptchaServerMock()
    @Autowired
    protected HttpClient http

    void cleanup() {
        http.clearCookies()
        recaptchaServer.reset()
    }
}
