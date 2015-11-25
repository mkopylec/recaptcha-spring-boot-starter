package com.github.mkopylec.recaptcha.security.login;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import com.github.mkopylec.recaptcha.RecaptchaProperties.Security;
import org.slf4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class LoginFailuresCountingHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger log = getLogger(LoginFailuresCountingHandler.class);

    protected final LoginFailuresManager failuresManager;
    protected final Security security;
    protected String usernameParameter;

    public LoginFailuresCountingHandler(LoginFailuresManager failuresManager, RecaptchaProperties recaptcha, RecaptchaAwareRedirectStrategy redirectStrategy) {
        this.failuresManager = failuresManager;
        security = recaptcha.getSecurity();
        setDefaultFailureUrl(security.getFailureUrl());
        setRedirectStrategy(redirectStrategy);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String username = getUsername(request);
        if (username != null) {
            log.debug("Adding login failure for username: {}", username);
            failuresManager.addLoginFailure(username);
        } else {
            log.error("Cannot add login failure for null username");
        }
        super.onAuthenticationFailure(request, response, exception);
    }

    public void setUsernameParameter(String usernameParameter) {
        this.usernameParameter = usernameParameter;
        RecaptchaAwareRedirectStrategy redirectStrategy = (RecaptchaAwareRedirectStrategy) getRedirectStrategy();
        if (redirectStrategy != null) {
            redirectStrategy.setUsernameParameter(usernameParameter);
        }
    }

    @Override
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        if (!RecaptchaAwareRedirectStrategy.class.isAssignableFrom(redirectStrategy.getClass())) {
            throw new IllegalArgumentException("Invalid redirect strategy. " + redirectStrategy + " must be instance of " + RecaptchaAwareRedirectStrategy.class.getName());
        }
        RecaptchaAwareRedirectStrategy strategy = (RecaptchaAwareRedirectStrategy) redirectStrategy;
        strategy.setUsernameParameter(usernameParameter);
        super.setRedirectStrategy(strategy);
    }

    protected String getUsername(HttpServletRequest request) {
        if (usernameParameter == null) {
            throw new IllegalStateException("Missing username parameter");
        }
        return request.getParameter(usernameParameter);
    }
}
