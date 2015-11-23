package com.github.mkopylec.recaptcha.security;

import com.github.mkopylec.recaptcha.RecaptchaProperties.Security;
import org.slf4j.Logger;
import org.springframework.security.core.AuthenticationException;
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

    public LoginFailuresCountingHandler(LoginFailuresManager failuresManager, Security security) {
        this.failuresManager = failuresManager;
        this.security = security;
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

    private String getUsername(HttpServletRequest request) {
        return request.getParameter(security.getUsernameParameter());
    }
}
