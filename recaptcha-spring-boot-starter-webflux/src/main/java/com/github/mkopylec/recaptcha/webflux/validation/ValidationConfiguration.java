package com.github.mkopylec.recaptcha.webflux.validation;

import com.github.mkopylec.recaptcha.commons.RecaptchaProperties;
import com.github.mkopylec.recaptcha.commons.RecaptchaProperties.Validation.Timeout;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import static com.github.mkopylec.recaptcha.commons.validation.Utils.toMilliseconds;
import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.springframework.web.reactive.function.client.WebClient.builder;

@Configuration
@EnableConfigurationProperties(RecaptchaProperties.class)
@ConditionalOnProperty(name = "recaptcha.testing.enabled", havingValue = "false", matchIfMissing = true)
public class ValidationConfiguration {

    private final RecaptchaProperties recaptcha;

    public ValidationConfiguration(RecaptchaProperties recaptcha) {
        this.recaptcha = recaptcha;
    }

    @Bean
    @ConditionalOnMissingBean
    public RecaptchaValidator webFluxRecaptchaValidator() {
        return new RecaptchaValidator(createWebClient(), recaptcha);
    }

    protected WebClient createWebClient() {
        Timeout timeout = recaptcha.getValidation().getTimeout();
        HttpClient httpClient = HttpClient.create().tcpConfiguration(client -> client
                .option(CONNECT_TIMEOUT_MILLIS, toMilliseconds(timeout.getConnect()))
                .doOnConnected(connection -> connection
                        .addHandlerLast(new ReadTimeoutHandler(toMilliseconds(timeout.getRead()), MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(toMilliseconds(timeout.getWrite()), MILLISECONDS))));
        return builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
