package com.github.mkopylec.recaptcha;

import org.apache.catalina.Context;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        run(TestApplication.class, args);
    }

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
        return new TomcatEmbeddedServletContainerFactory();
    }

    @Bean
    public EmbeddedServletContainerCustomizer customizer() {
        return new EmbeddedServletContainerCustomizer() {

            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
                TomcatContextCustomizer contextCustomizer = new TomcatContextCustomizer() {

                    @Override
                    public void customize(Context context) {
                        context.setCookieProcessor(new LegacyCookieProcessor());
                    }
                };
                tomcat.addContextCustomizers(contextCustomizer);
            }
        };
    }
}
