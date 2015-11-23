package com.github.mkopylec.recaptcha.security;

import com.github.mkopylec.recaptcha.RecaptchaProperties.Security;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

public class RecaptchaLoginEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public static final String SHOW_RECAPTCHA_QUERY_PARAM = "showRecaptcha";

    protected final LoginFailuresManager failuresManager;
    protected final Security security;

    public RecaptchaLoginEntryPoint(LoginFailuresManager failuresManager, Security security) {
        super(security.getLoginPage());
        this.failuresManager = failuresManager;
        this.security = security;
    }

    @Override
    protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        String loginUrl = super.determineUrlToUseForThisRequest(request, response, exception);
        if (failuresManager.getLoginFailuresCount() > security.getLoginFailuresThreshold()) {
            loginUrl = fromUriString(loginUrl)
                    .queryParam(SHOW_RECAPTCHA_QUERY_PARAM)
                    .toUriString();
        }
        return loginUrl;
    }
}
