package com.github.mkopylec.recaptcha.webmvc.test.utils

import com.github.mkopylec.recaptcha.test.utils.HttpClient
import com.github.mkopylec.recaptcha.test.utils.HttpResponse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

import static com.github.mkopylec.recaptcha.test.utils.HttpUtils.toFormData
import static org.springframework.http.HttpHeaders.COOKIE
import static org.springframework.http.HttpHeaders.SET_COOKIE

@Component
class WebMvcHttpClient extends HttpClient {

    private RestTemplate client = new RestTemplate()

    @Override
    protected <B> HttpResponse<B> post(String path, Class<B> responseType, Map<String, String> parameters = [:]) {
        def formData = toFormData(parameters)
        HttpHeaders headers = new HttpHeaders()
        if (cookies.get()) {
            headers.set(COOKIE, cookies.get())
        }
        def request = new HttpEntity<>(formData, headers)
        def response = client.postForEntity("$localhostUrl$path", request, responseType) as ResponseEntity<B>
        cookies.set(response.headers.get(SET_COOKIE) as String)

        return new HttpResponse<>(response.statusCodeValue, response.headers, response.body)
    }
}
