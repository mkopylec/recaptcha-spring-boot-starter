package com.github.mkopylec.recaptcha.security.login;

import com.github.mkopylec.recaptcha.security.RecaptchaAuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.security.web.WebAttributes.AUTHENTICATION_EXCEPTION;
import static org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter.ERROR_PARAMETER_NAME;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

public class RecaptchaAwareRedirectStrategy extends DefaultRedirectStrategy {

    public static final String RECAPTCHA_ERROR_PARAMETER_NAME = "recaptchaError";
    public static final String SHOW_RECAPTCHA_QUERY_PARAM = "showRecaptcha";

    protected final LoginFailuresManager failuresManager;

    public RecaptchaAwareRedirectStrategy(LoginFailuresManager failuresManager) {
        this.failuresManager = failuresManager;
    }

    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        UriComponentsBuilder urlBuilder = fromUriString(url);
        Object exception = getAuthenticationException(request);
        if (exception instanceof RecaptchaAuthenticationException) {
            urlBuilder.queryParam(RECAPTCHA_ERROR_PARAMETER_NAME);
        } else {
            urlBuilder.queryParam(ERROR_PARAMETER_NAME);
        }
        if (failuresManager.isRecaptchaRequired(request)) {
            urlBuilder.queryParam(SHOW_RECAPTCHA_QUERY_PARAM);
        }
        super.sendRedirect(request, response, urlBuilder.build(true).toUriString());
    }

    private Object getAuthenticationException(HttpServletRequest request) {
        Object exception = request.getSession(false).getAttribute(AUTHENTICATION_EXCEPTION);
        if (exception == null) {
            exception = request.getAttribute(AUTHENTICATION_EXCEPTION);
        }
        if (exception == null) {
            throw new IllegalStateException("Missing " + AUTHENTICATION_EXCEPTION + " session or request attribute");
        }
        return exception;
    }
}
