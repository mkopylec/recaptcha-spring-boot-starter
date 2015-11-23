package com.github.mkopylec.recaptcha.security;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import com.github.mkopylec.recaptcha.RecaptchaProperties.Security;
import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.validation.ValidationResult;
import org.slf4j.Logger;
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

import static com.github.mkopylec.recaptcha.validation.ErrorCode.MISSING_CAPTCHA_RESPONSE_PARAMETER;
import static com.github.mkopylec.recaptcha.validation.ErrorCode.MISSING_USERNAME_REQUEST_PARAMETER;
import static com.github.mkopylec.recaptcha.validation.ErrorCode.MISSING_USER_CAPTCHA_RESPONSE;
import static java.util.Collections.singletonList;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.util.Assert.notNull;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

public class RecaptchaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String RECAPTCHA_ERROR_QUERY_PARAM = "recaptchaError";
    public static final String RECAPTCHA_AUTHENTICATION_PRINCIPAL = "reCAPTCHA";

    private static final Logger log = getLogger(RecaptchaAuthenticationFilter.class);

    protected final RecaptchaValidator recaptchaValidator;
    protected final RecaptchaProperties recaptcha;
    protected final LoginFailuresManager failuresManager;

    public RecaptchaAuthenticationFilter(RecaptchaValidator recaptchaValidator, RecaptchaProperties recaptcha, LoginFailuresManager failuresManager) {
        super(new AntPathRequestMatcher(recaptcha.getSecurity().getLoginProcessingUrl(), POST.toString()));
        this.recaptchaValidator = recaptchaValidator;
        this.recaptcha = recaptcha;
        this.failuresManager = failuresManager;
        setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler() {

            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                super.onAuthenticationFailure(request, response, exception);
                //todo redirect with ?showRecaptcha when error code is MISSING_CAPTCHA_RESPONSE_PARAMETER
            }
        });
        setContinueChainBeforeSuccessfulAuthentication(true);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = getUsername(request);
        if (username == null) {
            throw new RecaptchaAuthenticationException(singletonList(MISSING_USERNAME_REQUEST_PARAMETER));
        }
        Authentication authentication = new PreAuthenticatedAuthenticationToken(RECAPTCHA_AUTHENTICATION_PRINCIPAL, null);
        if (failuresManager.isRecaptchaRequired(username)) {
            log.debug("reCAPTCHA required for username: {}", username);
            if (hasNoRecaptchaResponse(request)) {
                throw new RecaptchaAuthenticationException(singletonList(MISSING_CAPTCHA_RESPONSE_PARAMETER));
            }
            ValidationResult result = recaptchaValidator.validate(request);
            if (result.isSuccess()) {
                authentication.setAuthenticated(true);
                return authentication;
            }
            throw new RecaptchaAuthenticationException(result.getErrorCodes());
        }
        return authentication;
    }

    @Override
    public void afterPropertiesSet() {
        notNull(recaptchaValidator, "Missing recaptcha validator");
        notNull(recaptcha, "Missing recaptcha configuration properties");
        notNull(failuresManager, "Missing login failure manager");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = getUsername(request);
        log.debug("Clearing login failures for username: {}", username);
        failuresManager.resetLoginFailures(username);
    }

    private String resolveFailureUrl(Security recaptcha) {
        if (recaptcha.getFailureUrl() != null) {
            return recaptcha.getFailureUrl();
        }
        return fromUriString(recaptcha.getLoginProcessingUrl())
                .queryParam(RECAPTCHA_ERROR_QUERY_PARAM)
                .toUriString();
    }

    private String getUsername(HttpServletRequest request) {
        return request.getParameter(recaptcha.getSecurity().getUsernameParameter());
    }

    private boolean hasNoRecaptchaResponse(HttpServletRequest request) {
        return !request.getParameterMap().containsKey(recaptcha.getValidation().getResponseParameter());
    }
}
