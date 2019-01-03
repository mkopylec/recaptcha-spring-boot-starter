package com.github.mkopylec.recaptcha.webflux.test

import com.github.mkopylec.recaptcha.test.specification.ValidationSpec
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient

@AutoConfigureWebTestClient
class WebFluxValidationSpec extends ValidationSpec {
}
