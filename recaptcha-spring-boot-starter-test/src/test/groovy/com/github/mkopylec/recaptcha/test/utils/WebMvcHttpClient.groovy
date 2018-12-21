package com.github.mkopylec.recaptcha.test.utils

import com.github.mkopylec.recaptcha.test.webmvc.security.ResponseData
import com.github.mkopylec.recaptcha.commons.validation.ValidationResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap

import static com.github.mkopylec.recaptcha.test.Strings.WEBMVC_SPRING_PROFILE
import static org.springframework.http.HttpHeaders.COOKIE
import static org.springframework.http.HttpHeaders.SET_COOKIE

@Profile(WEBMVC_SPRING_PROFILE)
@Component
class WebMvcHttpClient implements HttpClient {

    @Autowired
    private TestRestTemplate client
    private ThreadLocal<String> cookies = new ThreadLocal<>()

    @Override
    Response<ValidationResult> validateRecaptcha(String userResponse) {
        if (userResponse == null) {
            return post('/testValidation/userResponse', ValidationResult)
        }
        return post('/testValidation/userResponse', ValidationResult, ['g-recaptcha-response': userResponse])
    }

    @Override
    Response<ValidationResult> validateRecaptchaWithIp(String userResponse) {
        return post('/testValidation/userResponseAndIp', ValidationResult, ['g-recaptcha-response': userResponse])
    }

    @Override
    Response<ValidationResult> validateRecaptchaInTestingMode() {
        return post('/testTesting/validate', ValidationResult)
    }

    @Override
    Response<ResponseData> getSecuredData() {
        return post('/testSecurity/getResponse', ResponseData)
    }

    @Override
    Response<Void> logIn(String username, String password, String recaptchaResponse) {
        def params = ['username': username, 'password': password]
        if (recaptchaResponse != null) {
            params['g-recaptcha-response'] = recaptchaResponse
        }
        return post('/login', Void, params)
    }

    @Override
    void clearCookies() {
        cookies.remove()
    }

    private <B> Response<B> post(String path, Class<B> responseType, Map<String, Object> parameters = [:]) {
        def params = new LinkedMultiValueMap<>()
        for (def parameter : parameters.entrySet()) {
            params.add(parameter.key, parameter.value)
        }
        HttpHeaders headers = new HttpHeaders()
        headers.set(COOKIE, cookies.get())
        def request = new HttpEntity<>(params, headers)
        def response = client.postForEntity(path, request, responseType) as ResponseEntity<B>
        cookies.set(response.headers.get(SET_COOKIE) as String)

        return new Response<>(response)
    }
}
