package com.github.mkopylec.recaptcha.security.login;

import com.github.mkopylec.recaptcha.security.RecaptchaAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.lang.reflect.Field;

import static org.springframework.util.ReflectionUtils.findField;
import static org.springframework.util.ReflectionUtils.makeAccessible;
import static org.springframework.util.ReflectionUtils.setField;

public class FormLoginConfigurerEnhancer {

    public static final String DEFAULT_USERNAME_PARAMETER = "username";
    protected static final String AUTHENTICATION_PROCESSING_FILTER_FIELD = "authFilter";

    protected final RecaptchaAuthenticationFilter authenticationFilter;
    protected final LoginFailuresClearingHandler successHandler;
    protected final LoginFailuresCountingHandler failureHandler;

    public FormLoginConfigurerEnhancer(RecaptchaAuthenticationFilter authenticationFilter, LoginFailuresClearingHandler successHandler, LoginFailuresCountingHandler failureHandler) {
        this.authenticationFilter = authenticationFilter;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    public FormLoginConfigurer<HttpSecurity> addRecaptchaSupport(FormLoginConfigurer<HttpSecurity> loginConfigurer) {
        Field authFilterField = findField(loginConfigurer.getClass(), AUTHENTICATION_PROCESSING_FILTER_FIELD, AbstractAuthenticationProcessingFilter.class);
        makeAccessible(authFilterField);
        setField(authFilterField, loginConfigurer, authenticationFilter);
        return loginConfigurer.usernameParameter(DEFAULT_USERNAME_PARAMETER)
                .successHandler(successHandler)
                .failureHandler(failureHandler);
    }
}
