package com.github.mkopylec.recaptcha.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        run(TestApplication.class, args);
    }

//    @Bean
//    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
//        return factory -> factory.addContextCustomizers(context -> context.setCookieProcessor(new LegacyCookieProcessor()));
//    }
}
