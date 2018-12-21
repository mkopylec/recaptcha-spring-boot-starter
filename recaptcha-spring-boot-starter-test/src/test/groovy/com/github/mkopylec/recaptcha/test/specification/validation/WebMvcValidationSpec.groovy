package com.github.mkopylec.recaptcha.test.specification.validation

import org.springframework.test.context.ActiveProfiles

import static com.github.mkopylec.recaptcha.test.Strings.WEBMVC_SPRING_PROFILE

@ActiveProfiles(WEBMVC_SPRING_PROFILE)
class WebMvcValidationSpec extends ValidationSpec {
}
