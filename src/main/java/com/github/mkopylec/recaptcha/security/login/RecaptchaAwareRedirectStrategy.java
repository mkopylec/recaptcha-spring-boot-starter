package com.github.mkopylec.recaptcha.security.login;

import com.github.mkopylec.recaptcha.RecaptchaProperties.Security;
import org.springframework.security.web.DefaultRedirectStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

public class RecaptchaAwareRedirectStrategy extends DefaultRedirectStrategy {

    public static final String SHOW_RECAPTCHA_QUERY_PARAM = "showRecaptcha";

    protected final LoginFailuresManager failuresManager;
    protected final Security security;

    public RecaptchaAwareRedirectStrategy(LoginFailuresManager failuresManager, Security security) {
        this.failuresManager = failuresManager;
        this.security = security;
    }

    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        if (failuresManager.isRecaptchaRequired(getUsername(request))) {
            url = addShowCaptchaParameter(url);
        }
        super.sendRedirect(request, response, url);
    }

    protected String addShowCaptchaParameter(String url) {
        return fromUriString(url)
                .queryParam(SHOW_RECAPTCHA_QUERY_PARAM)
                .toUriString();
    }

    protected String getUsername(HttpServletRequest request) {
        return request.getParameter(security.getUsernameParameter());
    }
}
