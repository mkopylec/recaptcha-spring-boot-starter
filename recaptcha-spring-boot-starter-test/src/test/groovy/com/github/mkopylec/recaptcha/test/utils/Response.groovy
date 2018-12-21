package com.github.mkopylec.recaptcha.test.utils

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.web.reactive.server.EntityExchangeResult

class Response<B> {

    private HttpStatus status
    private HttpHeaders headers
    private B body

    Response(ResponseEntity<B> responseEntity) {
        this.status = responseEntity.statusCode
        this.headers = responseEntity.headers
        this.body = responseEntity.body
    }

    Response(EntityExchangeResult<B> entityExchangeResult) {
        this.status = entityExchangeResult.status
        this.headers = entityExchangeResult.responseHeaders
        this.body = entityExchangeResult.responseBody
    }

    HttpStatus getStatus() {
        return status
    }

    HttpHeaders getHeaders() {
        return headers
    }

    B getBody() {
        return body
    }
}
