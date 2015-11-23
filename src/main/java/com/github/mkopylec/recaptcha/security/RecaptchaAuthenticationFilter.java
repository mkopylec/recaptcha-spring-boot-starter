package com.github.mkopylec.recaptcha.security;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import com.github.mkopylec.recaptcha.RecaptchaProperties.Security;
import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.validation.ValidationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.util.Assert.notNull;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

public class RecaptchaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String RECAPTCHA_ERROR_QUERY_PARAM = "recaptchaError";
    public static final String RECAPTCHA_AUTHENTICATION_PRINCIPAL = "reCAPTCHA";

    protected final RecaptchaValidator recaptchaValidator;
    protected final RecaptchaProperties recaptcha;
    protected final LoginFailuresManager failuresManager;

    public RecaptchaAuthenticationFilter(RecaptchaValidator recaptchaValidator, RecaptchaProperties recaptcha, LoginFailuresManager failuresManager) {
        super(new AntPathRequestMatcher(recaptcha.getSecurity().getLoginProcessingUrl(), POST.toString()));
        this.recaptchaValidator = recaptchaValidator;
        this.recaptcha = recaptcha;
        this.failuresManager = failuresManager;
        setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(resolveFailureUrl(recaptcha.getSecurity())));
        setContinueChainBeforeSuccessfulAuthentication(true);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Authentication authentication = new PreAuthenticatedAuthenticationToken(RECAPTCHA_AUTHENTICATION_PRINCIPAL, null);
        if (noRecaptchaResponse(request)) {
            return authentication;
        }
        ValidationResult result = recaptchaValidator.validate(request);
        if (result.isSuccess()) {
            authentication.setAuthenticated(true);
            return authentication;
        }
        throw new RecaptchaAuthenticationException(result.getErrorCodes());
    }

    @Override
    public void afterPropertiesSet() {
        notNull(recaptchaValidator, "Missing recaptcha validator");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        failuresManager.resetLoginFailures();
    }

    private String resolveFailureUrl(Security recaptcha) {
        if (recaptcha.getFailureUrl() != null) {
            return recaptcha.getFailureUrl();
        }
        return fromUriString(recaptcha.getLoginProcessingUrl())
                .queryParam(RECAPTCHA_ERROR_QUERY_PARAM)
                .toUriString();
    }

    private boolean noRecaptchaResponse(HttpServletRequest request) {
        return !request.getParameterMap().containsKey(recaptcha.getValidation().getResponseParameter());
    }
}
