package com.github.mkopylec.recaptcha.security.login;

import com.github.mkopylec.recaptcha.RecaptchaProperties.Security;
import org.slf4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

public abstract class LoginFailuresCountingHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger log = getLogger(LoginFailuresCountingHandler.class);

    protected final LoginFailuresManager failuresManager;
    protected final Security security;
    protected final String queryParameter;

    public LoginFailuresCountingHandler(LoginFailuresManager failuresManager, Security security, String queryParameter) {
        this.failuresManager = failuresManager;
        this.security = security;
        this.queryParameter = queryParameter;
        setDefaultFailureUrl(resolveFailureUrl(security));
        setRedirectStrategy(new RecaptchaAwareRedirectStrategy(failuresManager, security));
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

    protected String resolveFailureUrl(Security recaptcha) {
        if (recaptcha.getFailureUrl() != null) {
            return recaptcha.getFailureUrl();
        }
        return fromUriString(recaptcha.getLoginProcessingUrl())
                .queryParam(queryParameter)
                .toUriString();
    }

    protected String getUsername(HttpServletRequest request) {
        return request.getParameter(security.getUsernameParameter());
    }
}
