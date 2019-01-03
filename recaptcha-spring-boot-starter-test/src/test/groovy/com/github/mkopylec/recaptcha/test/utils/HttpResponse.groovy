package com.github.mkopylec.recaptcha.test.utils

import org.springframework.util.MultiValueMap

class HttpResponse<B> {

    private int status
    private MultiValueMap<String, String> headers
    private B body

    HttpResponse(int status, MultiValueMap<String, String> headers, B body) {
        this.status = status
        this.headers = headers
        this.body = body
    }

    int getStatus() {
        return status
    }

    MultiValueMap<String, String> getHeaders() {
        return headers
    }

    B getBody() {
        return body
    }
}
