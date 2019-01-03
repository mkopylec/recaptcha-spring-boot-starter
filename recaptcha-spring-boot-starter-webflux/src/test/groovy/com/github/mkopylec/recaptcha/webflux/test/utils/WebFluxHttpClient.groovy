package com.github.mkopylec.recaptcha.webflux.test.utils

import com.github.mkopylec.recaptcha.test.utils.HttpClient
import com.github.mkopylec.recaptcha.test.utils.HttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

import static com.github.mkopylec.recaptcha.test.utils.HttpUtils.toFormData
import static org.springframework.http.HttpHeaders.COOKIE
import static org.springframework.http.HttpHeaders.SET_COOKIE
import static org.springframework.web.reactive.function.BodyInserters.fromFormData
import static org.springframework.web.reactive.function.client.WebClient.create

@Component
class WebFluxHttpClient extends HttpClient {

    private WebClient client = create()

    @Override
    protected <B> HttpResponse<B> post(String path, Class<B> responseType, Map<String, String> parameters = [:]) {
        def formData = toFormData(parameters)
        def request = client
                .post()
                .uri("$localhostUrl$path")
                .body(fromFormData(formData))
        if (cookies.get()) {
            request.header(COOKIE, cookies.get())
        }
        def response = request
                .exchange()
                .block()
        cookies.set(response.headers().asHttpHeaders().get(SET_COOKIE) as String)

        return new HttpResponse<>(response.rawStatusCode(), response.headers().asHttpHeaders(), response.bodyToMono(responseType).block())
    }
}
