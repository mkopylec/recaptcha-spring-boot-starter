package com.github.mkopylec.recaptcha.test.specification.validation

import com.github.mkopylec.recaptcha.test.TestApplication
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner

import static com.github.mkopylec.recaptcha.test.Strings.WEBFLUX_SPRING_PROFILE
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@ActiveProfiles(WEBFLUX_SPRING_PROFILE)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = TestApplication, properties = ['spring.main.web-application-type: reactive'])
//@TestPropertySource(properties = ['spring.main.web-application-type: reactive'])
class WebFluxValidationSpec extends ValidationSpec {
}
