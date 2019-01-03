package com.github.mkopylec.recaptcha.test.utils

import com.github.mkopylec.recaptcha.commons.validation.ValidationResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.context.WebServerApplicationContext

abstract class HttpClient {

    @Autowired
    private WebServerApplicationContext applicationContext
    protected ThreadLocal<String> cookies = new ThreadLocal<>()

    HttpResponse<ValidationResult> validateRecaptcha(String userResponse) {
        if (userResponse == null) {
            return post('/testValidation/userResponse', ValidationResult)
        }
        return post('/testValidation/userResponse', ValidationResult, ['g-recaptcha-response': userResponse])
    }

    HttpResponse<ValidationResult> validateRecaptchaWithIp(String userResponse) {
        return post('/testValidation/userResponseAndIp', ValidationResult, ['g-recaptcha-response': userResponse])
    }

    HttpResponse<ValidationResult> validateRecaptchaInTestingMode() {
        return post('/testTesting/validate', ValidationResult)
    }

    HttpResponse<ResponseBody> getSecuredData() {
        return post('/testSecurity/getResponse', ResponseBody)
    }

    HttpResponse<Void> logIn(String username, String password) {
        return logIn(username, password, null)
    }

    HttpResponse<Void> logIn(String username, String password, String recaptchaResponse) {
        def params = ['username': username, 'password': password]
        if (recaptchaResponse != null) {
            params['g-recaptcha-response'] = recaptchaResponse
        }
        return post('/login', Void, params)
    }

    void clearCookies() {
        cookies.remove()
    }

    String getLocalhostUrl() {
        return "http://localhost:$applicationContext.webServer.port"
    }

    protected abstract <B> HttpResponse<B> post(String path, Class<B> responseType, Map<String, String> parameters = [:])
}