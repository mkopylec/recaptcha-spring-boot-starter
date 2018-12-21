package com.github.mkopylec.recaptcha.test.utils

import com.github.mkopylec.recaptcha.test.webmvc.security.ResponseData
import com.github.mkopylec.recaptcha.commons.validation.ValidationResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.test.web.reactive.server.WebTestClient

import static HttpUtils.toFormData
import static com.github.mkopylec.recaptcha.test.Strings.WEBFLUX_SPRING_PROFILE
import static org.springframework.http.HttpHeaders.COOKIE
import static org.springframework.http.HttpHeaders.SET_COOKIE
import static org.springframework.web.reactive.function.BodyInserters.fromFormData

@Profile(WEBFLUX_SPRING_PROFILE)
@Component
class WebFluxHttpClient implements HttpClient {

    @Autowired
    private WebTestClient client
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
        def formData = toFormData(parameters)
        def result = client.post()
//                .header(COOKIE, cookies.get())
                .body(fromFormData(formData))
                .exchange()
                .expectBody(responseType)
                .returnResult()
        cookies.set(result.responseHeaders.get(SET_COOKIE) as String)

        return new Response<>(result)
    }
}
