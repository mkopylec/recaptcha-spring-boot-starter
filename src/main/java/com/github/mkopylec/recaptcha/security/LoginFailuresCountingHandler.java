package com.github.mkopylec.recaptcha.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFailuresCountingHandler extends SimpleUrlAuthenticationFailureHandler {

    protected final LoginFailuresManager failuresManager;

    public LoginFailuresCountingHandler(LoginFailuresManager failuresManager) {
        this.failuresManager = failuresManager;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        failuresManager.addLoginFailure();
        super.onAuthenticationFailure(request, response, exception);
    }
}
