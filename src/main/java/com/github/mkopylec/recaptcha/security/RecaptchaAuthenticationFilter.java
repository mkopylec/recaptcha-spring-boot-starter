package com.github.mkopylec.recaptcha.security;

import com.github.mkopylec.recaptcha.RecaptchaProperties.Security;
import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.validation.ValidationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RecaptchaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private RecaptchaValidator recaptchaValidator;

    public RecaptchaAuthenticationFilter(RequestMatcher requestMatcher, RecaptchaValidator recaptchaValidator, Security recaptcha) {
        super(requestMatcher);
        this.recaptchaValidator = recaptchaValidator;
        setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(recaptcha.getFailureUrl()));
        setContinueChainBeforeSuccessfulAuthentication(true);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        ValidationResult result = recaptchaValidator.validate(request);
        if (result.isSuccess()) {
            return new PreAuthenticatedAuthenticationToken("reCAPTCHA", null);
        }
        throw new RecaptchaAuthenticationException(result.getErrorCodes());
    }

    @Override
    public void afterPropertiesSet() {
    }
}
