package com.github.mkopylec.recaptcha.webflux.test

import com.github.mkopylec.recaptcha.test.specification.SecuritySpec
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient

@AutoConfigureWebTestClient
class WebFluxSecuritySpec extends SecuritySpec {
}
