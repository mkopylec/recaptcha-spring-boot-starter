package com.github.mkopylec.recaptcha.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

import static org.springframework.boot.SpringApplication.run;

@EnableWebFlux
@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        run(TestApplication.class, args);
    }

    // TODO Oprzec wszystko na Jetty bo wiremock tego uzywa i SB jak widzi Jetty to probuje je uruchomic, ale wiremock ma stara (?) zaleznosc do jetty i brakuje jakiejs metody
//    @Bean
//    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
//        return factory -> factory.addContextCustomizers(context -> context.setCookieProcessor(new LegacyCookieProcessor()));
//    }
}
