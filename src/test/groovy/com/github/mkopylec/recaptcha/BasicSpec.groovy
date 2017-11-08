package com.github.mkopylec.recaptcha

import com.github.mkopylec.recaptcha.security.ResponseData
import com.github.mkopylec.recaptcha.validation.ValidationResult
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import static org.springframework.http.HttpHeaders.COOKIE
import static org.springframework.http.HttpHeaders.SET_COOKIE

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = TestApplication)
abstract class BasicSpec extends Specification {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8081)

    @LocalServerPort
    protected int port
    @Autowired
    private TestRestTemplate restTemplate

    private ThreadLocal<String> cookies = new ThreadLocal<>()

    protected ResponseEntity<ValidationResult> validateRecaptcha(String userResponse) {
        if (userResponse == null) {
            return post('/testValidation/userResponse', ValidationResult)
        }
        return post('/testValidation/userResponse', ValidationResult, ['g-recaptcha-response': userResponse])
    }

    protected ResponseEntity<ValidationResult> validateRecaptchaWithIp(String userResponse) {
        return post('/testValidation/userResponseAndIp', ValidationResult, ['g-recaptcha-response': userResponse])
    }

    protected ResponseEntity<ValidationResult> validateRecaptchaInTestingMode() {
        return post('/testTesting/validate', ValidationResult)
    }

    protected ResponseEntity<ResponseData> getSecuredData() {
        return post('/testSecurity/getResponse', ResponseData)
    }

    protected ResponseEntity<Object> logIn(String username, String password, String recaptchaResponse = null) {
        def params = ['username': username, 'password': password]
        if (recaptchaResponse != null) {
            params['g-recaptcha-response'] = recaptchaResponse
        }
        return post('/login', Object, params)
    }

    private <T> ResponseEntity<T> post(String path, Class<T> responseType, Map<String, Object> parameters = [:]) {
        def params = new LinkedMultiValueMap<>()
        for (def parameter : parameters.entrySet()) {
            params.add(parameter.key, parameter.value)
        }
        HttpHeaders headers = new HttpHeaders()
        headers.set(COOKIE, cookies.get())
        def request = new HttpEntity<>(params, headers)
        def response = restTemplate.postForEntity(path, request, responseType) as ResponseEntity<T>
        cookies.set(response.headers.get(SET_COOKIE) as String)

        return response
    }

    void cleanup() {
        cookies.remove()
    }
}
