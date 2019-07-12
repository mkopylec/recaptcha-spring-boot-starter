package com.github.mkopylec.recaptcha.validation;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import com.github.mkopylec.recaptcha.RecaptchaProperties.Validation.Timeout;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration("recaptchaValidationConfiguration")
@EnableConfigurationProperties(RecaptchaProperties.class)
@ConditionalOnProperty(name = "recaptcha.testing.enabled", havingValue = "false", matchIfMissing = true)
public class ValidationConfiguration {

    private final RecaptchaProperties recaptcha;

    public ValidationConfiguration(RecaptchaProperties recaptcha) {
        this.recaptcha = recaptcha;
    }

    @Bean
    @ConditionalOnMissingBean
    public RecaptchaValidator userResponseValidator(IpAddressResolver ipAddressResolver) {
        return new DefaultRecaptchaValidator(createRestTemplate(), recaptcha, ipAddressResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public IpAddressResolver ipAddressResolver() {
        return new IpAddressResolver();
    }

    protected RestTemplate createRestTemplate() {
        Timeout timeout = recaptcha.getValidation().getTimeout();
        OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory();
        requestFactory.setConnectTimeout(toMilliseconds(timeout.getConnect()));
        requestFactory.setReadTimeout(toMilliseconds(timeout.getRead()));
        requestFactory.setWriteTimeout(toMilliseconds(timeout.getWrite()));
        return new RestTemplate(requestFactory);
    }

    protected int toMilliseconds(Duration duration) {
        return (int) duration.toMillis();
    }
}
