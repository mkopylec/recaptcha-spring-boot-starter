package com.github.mkopylec.recaptcha;

import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        run(TestApplication.class, args);
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
        return factory -> factory.addContextCustomizers(context -> context.setCookieProcessor(new LegacyCookieProcessor()));
    }
}
