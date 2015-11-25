package com.github.mkopylec.recaptcha.security.login;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import com.github.mkopylec.recaptcha.RecaptchaProperties.Security;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class LoginFailuresClearingHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger log = getLogger(LoginFailuresClearingHandler.class);

    protected final LoginFailuresManager failuresManager;
    protected final Security security;
    protected String usernameParameter;

    public LoginFailuresClearingHandler(LoginFailuresManager failuresManager, RecaptchaProperties recaptcha) {
        this.failuresManager = failuresManager;
        security = recaptcha.getSecurity();
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = getUsername(request);
        log.debug("Clearing login failures for username: {}", username);
        failuresManager.resetLoginFailures(username);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    public void setUsernameParameter(String usernameParameter) {
        this.usernameParameter = usernameParameter;
    }

    protected String getUsername(HttpServletRequest request) {
        if (usernameParameter == null) {
            throw new IllegalStateException("Missing username parameter");
        }
        return request.getParameter(usernameParameter);
    }
}
